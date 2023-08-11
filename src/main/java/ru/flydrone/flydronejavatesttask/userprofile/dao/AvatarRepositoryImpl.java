package ru.flydrone.flydronejavatesttask.userprofile.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.flydrone.flydronejavatesttask.userprofile.dto.UserProfileWithAvatarDTO;
import ru.flydrone.flydronejavatesttask.yandexcloud.dao.YandexCloudRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class AvatarRepositoryImpl implements AvatarRepository {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

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

//    @Autowired
//    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
//    private final YandexCloudRepository yandexCloudRepository;
//
//    @Autowired
//    AvatarRepositoryImpl(YandexCloudRepository repository) {
//        this.yandexCloudRepository = repository;
//    }
//
//    @Override
//    public void insertAvatar(Long userProfileId, String avatarId) {
//        final String INSERT_FILE_SQL = "INSERT INTO flydrone.file (external_id) " +
//                "VALUES :external_id";
//
//        Map<String, Object> parameters = new HashMap<>(1);
//        parameters.put("external_id", avatarId);
//        namedParameterJdbcTemplate.update(INSERT_FILE_SQL, parameters);
//    }
//
//    @Override
//    public void deleteAvatar(String avatarId) {
//        final String UPDATE_OLD_FILE_SQL = "UPDATE flydrone.file SET is_deleted = TRUE " +
//                "WHERE external_id = :external_id";
//
//        Map<String, Object> parameters = new HashMap<>(1);
//        parameters.put("external_id", avatarId);
//        namedParameterJdbcTemplate.update(UPDATE_OLD_FILE_SQL, parameters);
//    }
        }
