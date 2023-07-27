package ru.flydrone.flydronejavatesttask.userprofile.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.services.s3.model.S3Object;
import ru.flydrone.flydronejavatesttask.userprofile.dto.UserProfileWithAvatarDTO;
import ru.flydrone.flydronejavatesttask.userprofile.dto.UserProfileWithAvatarRowMapper;
import ru.flydrone.flydronejavatesttask.userprofile.dto.UserProfileDTO;
import ru.flydrone.flydronejavatesttask.yandexcloud.dao.YandexCloudRepository;

import java.util.*;

@Repository
public class UserProfileRepositoryImpl implements UserProfileRepository {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final YandexCloudRepository yandexCloudRepository;

    @Autowired
    UserProfileRepositoryImpl(YandexCloudRepository repository) {
        this.yandexCloudRepository = repository;
    }

    @Override
    public void updateUserProfile(UserProfileDTO userProfile) {
        final String UPDATE_SQL =
                "UPDATE flydrone_profile.user_profile SET" +
                        " first_name = :first_name," +
                        " last_name = :last_name," +
                        " patronymic = :patronymic," +
                        " birthdate = :birthdate" +
                        " WHERE id = :id;";

        Map<String, Object> parameters = new HashMap<>(5);
        parameters.put("first_name", userProfile.getFirstName());
        parameters.put("last_name", userProfile.getLastName());
        parameters.put("patronymic", userProfile.getPatronymic());
        parameters.put("birthdate", userProfile.getBirthdate());
        parameters.put("id", userProfile.getId());

        namedParameterJdbcTemplate.update(UPDATE_SQL, parameters);
    }

    @Override
    public Long insertUserProfile(UserProfileDTO userProfile) {
        final Long userProfileId;

        Map<String, Object> parameters = new HashMap<>(4);
        parameters.put("first_name", userProfile.getFirstName());
        parameters.put("last_name", userProfile.getLastName());
        parameters.put("patronymic", userProfile.getPatronymic());
        parameters.put("birthdate", userProfile.getBirthdate());

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(this.namedParameterJdbcTemplate.getJdbcTemplate())
                .withSchemaName("flydrone_profile")
                .withTableName("user_profile")
                .usingGeneratedKeyColumns("id");

        userProfileId = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        userProfile.setId(userProfileId);

        return userProfileId;
    }

    @Override
    public Optional<Long> saveUserProfile(UserProfileDTO userProfile) {
        Optional<Long> userProfileId = Optional.empty();

        if (userProfile.getId() != null) {
            int updatedRowCount;
            final String UPDATE_SQL =
                    "UPDATE flydrone_profile.user_profile SET" +
                            " first_name = :first_name," +
                            " last_name = :last_name," +
                            " patronymic = :patronymic," +
                            " birthdate = :birthdate" +
                            " WHERE id = :id;";

            Map<String, Object> parameters = new HashMap<>(5);
            parameters.put("first_name", userProfile.getFirstName());
            parameters.put("last_name", userProfile.getLastName());
            parameters.put("patronymic", userProfile.getPatronymic());
            parameters.put("birthdate", userProfile.getBirthdate());
            parameters.put("id", userProfile.getId());

            updatedRowCount = namedParameterJdbcTemplate.update(UPDATE_SQL, parameters);

            if (updatedRowCount > 0) {
                userProfileId = Optional.of(userProfile.getId());
            }
        } else {
            Map<String, Object> parameters = new HashMap<>(4);
            parameters.put("first_name", userProfile.getFirstName());
            parameters.put("last_name", userProfile.getLastName());
            parameters.put("patronymic", userProfile.getPatronymic());
            parameters.put("birthdate", userProfile.getBirthdate());

            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(this.namedParameterJdbcTemplate.getJdbcTemplate())
                    .withSchemaName("flydrone_profile")
                    .withTableName("user_profile")
                    .usingGeneratedKeyColumns("id");

            userProfileId = Optional.of(simpleJdbcInsert.executeAndReturnKey(parameters).longValue());
            userProfile.setId(userProfileId.get());
        }

        return userProfileId;
    }

    @Override
    @Transactional
    public Optional<Long> deleteUserProfile(Long id) {
        Optional<Long> deletedAvatarUserProfileId = deleteAvatar(id);
        if (deletedAvatarUserProfileId.isEmpty()) {
            return Optional.empty();
        }
        final String DELETE_SQL = "DELETE FROM flydrone_profile.user_profile WHERE id = :id";
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("id", id);
        int deletedRowCount = namedParameterJdbcTemplate.update(DELETE_SQL, parameters);
        return deletedRowCount == 0 ? Optional.empty() : Optional.of(id);
    }

    @Override
    public Optional<UserProfileDTO> getUserProfile(Long id) {
        final String SELECT_SQL = "SELECT " +
                "first_name, " +
                "last_name, " +
                "birthdate, " +
                "id, " +
                "patronymic " +
                "FROM flydrone_profile.user_profile " +
                "WHERE id = :id";
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("id", id);
        try {
            return Optional.of(namedParameterJdbcTemplate.queryForObject(SELECT_SQL, parameters, new UserProfileRowMapper()));
        } catch (IncorrectResultSizeDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserProfileWithAvatarDTO> getUserProfileWithAvatar(Long id) {
        final String SELECT_SQL = "SELECT " +
                "first_name, " +
                "last_name, " +
                "birthdate, " +
                "id, " +
                "patronymic, " +
                "avatar_file_id " +
                "FROM flydrone_profile.user_profile " +
                "WHERE id = :id";

        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("id", id);

        try {
            return Optional.of(namedParameterJdbcTemplate.queryForObject(SELECT_SQL, parameters, new UserProfileWithAvatarRowMapper()));
        } catch (IncorrectResultSizeDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public Optional<Long> saveAvatar(Long userProfileId, MultipartFile avatar) {
        Optional<UserProfileWithAvatarDTO> userProfileWithAvatar = getUserProfileWithAvatar(userProfileId);

        if (userProfileWithAvatar.isEmpty()) {
            return Optional.empty();
        }

        final UUID externalId = java.util.UUID.randomUUID();

        Optional<String> savedObjectExternalId = yandexCloudRepository.saveObject(
                externalId.toString(),
                avatar
        );

        final String INSERT_FILE_SQL = "INSERT INTO flydrone.file (external_id) " +
                "VALUES :external_id";

        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("external_id", savedObjectExternalId.get());
        namedParameterJdbcTemplate.update(INSERT_FILE_SQL, parameters);

        final String UPDATE_OLD_FILE_SQL = "UPDATE flydrone.file SET is_deleted = TRUE " +
                "WHERE external_id = :external_id";

        parameters = new HashMap<>(1);
        parameters.put("external_id", userProfileWithAvatar.get().getExternalAvatarId());
        namedParameterJdbcTemplate.update(UPDATE_OLD_FILE_SQL, parameters);

        final String UPDATE_USER_PROFILE_SQL = "UPDATE flydrone_profile.user_profile " +
                "SET avatar_file_id = :avatar_file_id " +
                "WHERE id = :user_profile_id";

        parameters = new HashMap<>(2);
        parameters.put("user_profile_id", userProfileId);
        parameters.put("avatar_file_id", savedObjectExternalId.get());
        int updatedRowCount = namedParameterJdbcTemplate.update(UPDATE_USER_PROFILE_SQL, parameters);

        return updatedRowCount == 0 ? Optional.empty() : Optional.of(userProfileId);

//        final String UPSERT_SQL = "INSERT INTO flydrone_profile.avatar (user_profile_id, external_id) " +
//                "VALUES (:user_profile_id, :external_id) " +
//                "ON CONFLICT (user_profile_id) DO UPDATE SET " +
//                "external_id = EXCLUDED.external_id";
//
//        Optional<UserProfileWithAvatarDTO> userProfileWithAvatar = getUserProfileWithAvatar(userProfileId);
//
//        if (userProfileWithAvatar.isEmpty()) {
//            return Optional.empty();
//        }
//
//        final UUID externalId = java.util.UUID.randomUUID();
//
//        String existingAvatarExternalId = userProfileWithAvatar.get().getExternalAvatarId();
//
//        Optional<String> savedObjectExternalId = yandexCloudRepository.saveObject(
//                externalId.toString(),
//                avatar
//        );
//
//        if (savedObjectExternalId.isEmpty()) {
//            return Optional.empty();
//        }
//
//        Map<String, Object> parameters = new HashMap<>(2);
//
//        parameters.put("user_profile_id", userProfileId);
//        parameters.put("external_id", externalId.toString());
//
//        try {
//            namedParameterJdbcTemplate.update(UPSERT_SQL, parameters);
//        } catch (DataAccessException ex) {
//            yandexCloudRepository.deleteObject(externalId.toString());
//            return Optional.empty();
//        }
//
//        if (existingAvatarExternalId != null) {
//            yandexCloudRepository.deleteObject(existingAvatarExternalId);
//        }
//
//        return Optional.of(userProfileId);
    }

    @Override
    public Optional<S3Object> getAvatar(Long userProfileId) {
        final Optional<String> avatarExternalId;
        final String SELECT_SQL = "SELECT " +
                "avatar_file_id " +
                "FROM flydrone_profile.user_profile " +
                "WHERE user_profile_id = :user_profile_id";
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("user_profile_id", userProfileId);
        try {
            avatarExternalId = Optional.of(namedParameterJdbcTemplate.queryForObject(SELECT_SQL, parameters, String.class));
            return yandexCloudRepository.getObject(avatarExternalId.get());
        } catch (IncorrectResultSizeDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public Optional<Long> deleteAvatar(Long userProfileId) {
        Optional<UserProfileWithAvatarDTO> userProfileWithAvatar = getUserProfileWithAvatar(userProfileId);
        String fileExternalId = userProfileWithAvatar.map(u ->  u.getExternalAvatarId()).orElse(null);

        final String UPDATE_USER_PROFILE_SQL = "UPDATE flydrone_profile.user_profile " +
                "SET avatar_file_id = NULL " +
                "WHERE user_profile_id = :user_profile_id";
                Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("user_profile_id", userProfileId);
        int updatedRowCount = namedParameterJdbcTemplate.update(UPDATE_USER_PROFILE_SQL, parameters);

        final String UPDATE_FILE_SQL = "UPDATE flydrone.file " +
                "SET is_deleted = TRUE " +
                "WHERE external_id = :file_external_id";
        parameters = new HashMap<>(1);
        parameters.put("file_external_id", fileExternalId);
        namedParameterJdbcTemplate.update(UPDATE_FILE_SQL, parameters);

        return updatedRowCount == 0 ? Optional.empty() : Optional.of(userProfileId);

//        Optional<UserProfileWithAvatarDTO> userProfileWithAvatar = getUserProfileWithAvatar(userProfileId);
//        if (userProfileWithAvatar.isEmpty() || userProfileWithAvatar.get().getExternalAvatarId() == null) {
//            return Optional.empty();
//        }
//        Optional<String> deletedAvatarExternalId = yandexCloudRepository
//                .deleteObject(userProfileWithAvatar.get().getExternalAvatarId());
//
//        if (deletedAvatarExternalId.isEmpty()) {
//            return Optional.empty();
//        }
//
//        final String DELETE_SQL = "DELETE FROM flydrone_profile.avatar WHERE user_profile_id = :user_profile_id";
//        Map<String, Object> parameters = new HashMap<>(1);
//        parameters.put("user_profile_id", userProfileId);
//        int deletedRowCount = namedParameterJdbcTemplate.update(DELETE_SQL, parameters);
//        return deletedRowCount == 0 ? Optional.empty() : Optional.of(userProfileId);
    }
}
