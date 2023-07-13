package ru.flydrone.flydronejavatesttask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    public Optional<Long> deleteUserProfile(Long id) {
        Optional<Long> deletedUserProfileId;
        final String DELETE_SQL = "DELETE FROM flydrone_profile.user_profile WHERE id = :id";
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("id", id);
        int deletedRowCount = namedParameterJdbcTemplate.update(DELETE_SQL, parameters);
        deletedUserProfileId = deletedRowCount == 0 ? Optional.empty() : Optional.of(id);
        return deletedUserProfileId;
    }

    @Override
    public Optional<UserProfileDTO> getUserProfile(Long id) {
        final Optional<UserProfileDTO> userProfile;
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
        List<UserProfileDTO> userProfileList = namedParameterJdbcTemplate.query(SELECT_SQL, parameters, new UserProfileRowMapper());
        userProfile = userProfileList.size() > 0 ? Optional.of(userProfileList.get(0)) : Optional.empty();
        return userProfile;
    }

    @Override
    public Optional<UserProfileWithAvatarDTO> getUserProfileWithAvatar(Long id) {
        final Optional<UserProfileWithAvatarDTO> userProfileWithAvatar;
        final String SELECT_SQL = "SELECT " +
                "up.first_name, " +
                "up.last_name, " +
                "up.birthdate, " +
                "up.id, " +
                "up.patronymic, " +
                "a.external_id avatar_external_id " +
                "FROM flydrone_profile.user_profile up " +
                "LEFT JOIN flydrone_profile.avatar a ON a.user_profile_id = up.id " +
                "WHERE id = :id";

        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("id", id);
        List<UserProfileWithAvatarDTO> userProfileWithAvatarList = namedParameterJdbcTemplate.query(SELECT_SQL, parameters, new UserProfileWithAvatarRowMapper());
        userProfileWithAvatar = userProfileWithAvatarList.size() > 0 ? Optional.of(userProfileWithAvatarList.get(0)) : Optional.empty();
        return userProfileWithAvatar;
    }

    @Override
    public Optional<Long> saveAvatar(Long userProfileId, MultipartFile avatar) {
        final String UPSERT_SQL = "INSERT INTO flydrone_profile.avatar (user_profile_id, external_id) " +
                "VALUES (:user_profile_id, :external_id) " +
                "ON CONFLICT (user_profile_id) DO UPDATE SET " +
                "external_id = EXCLUDED.external_id";

        Optional<UserProfileWithAvatarDTO> userProfileWithAvatar = getUserProfileWithAvatar(userProfileId);

        if (userProfileWithAvatar.isEmpty()) {
            return Optional.empty();
        }

        final UUID externalId = java.util.UUID.randomUUID();

        String existingAvatarExternalId = userProfileWithAvatar.get().getExternalAvatarId();

        if (existingAvatarExternalId != null) {
           Optional<String> deletedObjectExternalId = yandexCloudRepository.deleteObject(existingAvatarExternalId);
           if (deletedObjectExternalId.isEmpty()) {
               return Optional.empty();
           }
        }

        Optional<String> savedObjectExternalId = yandexCloudRepository.saveObject(
                externalId.toString(),
                avatar
        );

        if (savedObjectExternalId.isEmpty()) {
            return Optional.empty();
        }

        Map<String, Object> parameters = new HashMap<>(2);

        parameters.put("user_profile_id", userProfileId);
        parameters.put("external_id", externalId.toString());

        namedParameterJdbcTemplate.update(UPSERT_SQL, parameters);

        return Optional.of(userProfileId);
    }
}
