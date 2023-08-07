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
import ru.flydrone.flydronejavatesttask.userprofile.dto.UserProfileDTO;
import ru.flydrone.flydronejavatesttask.yandexcloud.dao.YandexCloudRepository;

import java.util.*;

@Repository
public class UserProfileRepositoryImpl implements UserProfileRepository {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

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

//    @Override
//    public Optional<Long> saveUserProfile(UserProfileDTO userProfile) {
//        Optional<Long> userProfileId = Optional.empty();
//
//        if (userProfile.getId() != null) {
//            int updatedRowCount;
//            final String UPDATE_SQL =
//                    "UPDATE flydrone_profile.user_profile SET" +
//                            " first_name = :first_name," +
//                            " last_name = :last_name," +
//                            " patronymic = :patronymic," +
//                            " birthdate = :birthdate" +
//                            " WHERE id = :id;";
//
//            Map<String, Object> parameters = new HashMap<>(5);
//            parameters.put("first_name", userProfile.getFirstName());
//            parameters.put("last_name", userProfile.getLastName());
//            parameters.put("patronymic", userProfile.getPatronymic());
//            parameters.put("birthdate", userProfile.getBirthdate());
//            parameters.put("id", userProfile.getId());
//
//            updatedRowCount = namedParameterJdbcTemplate.update(UPDATE_SQL, parameters);
//
//            if (updatedRowCount > 0) {
//                userProfileId = Optional.of(userProfile.getId());
//            }
//        } else {
//            Map<String, Object> parameters = new HashMap<>(4);
//            parameters.put("first_name", userProfile.getFirstName());
//            parameters.put("last_name", userProfile.getLastName());
//            parameters.put("patronymic", userProfile.getPatronymic());
//            parameters.put("birthdate", userProfile.getBirthdate());
//
//            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(this.namedParameterJdbcTemplate.getJdbcTemplate())
//                    .withSchemaName("flydrone_profile")
//                    .withTableName("user_profile")
//                    .usingGeneratedKeyColumns("id");
//
//            userProfileId = Optional.of(simpleJdbcInsert.executeAndReturnKey(parameters).longValue());
//            userProfile.setId(userProfileId.get());
//        }
//
//        return userProfileId;
//    }

    @Override
    public void deleteUserProfile(Long id) {
        final String DELETE_SQL = "DELETE FROM flydrone_profile.user_profile WHERE id = :id";
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("id", id);
        namedParameterJdbcTemplate.update(DELETE_SQL, parameters);
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
    public void updateAvatarId(Long userProfileId, String avatarId) {
        final String UPDATE_USER_PROFILE_SQL = "UPDATE flydrone_profile.user_profile " +
                "SET avatar_file_id = :avatar_file_id " +
                "WHERE id = :user_profile_id";

        Map<String, Object> parameters = new HashMap<>(2);
        parameters.put("user_profile_id", userProfileId);
        parameters.put("avatar_file_id", avatarId);
        namedParameterJdbcTemplate.update(UPDATE_USER_PROFILE_SQL, parameters);
    }

    @Override
    public void deleteAvatar(Long userProfileId) {
        final String UPDATE_USER_PROFILE_SQL = "UPDATE flydrone_profile.user_profile " +
                "SET avatar_file_id = NULL " +
                "WHERE user_profile_id = :user_profile_id";
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("user_profile_id", userProfileId);
        namedParameterJdbcTemplate.update(UPDATE_USER_PROFILE_SQL, parameters);
    }
}
