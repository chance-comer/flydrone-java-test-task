package ru.flydrone.flydronejavatesttask.userprofile.dao;

import com.amazonaws.services.s3.model.S3Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.flydrone.flydronejavatesttask.DataNotFoundException;
import ru.flydrone.flydronejavatesttask.userprofile.dto.UserProfileWithAvatarDTO;
import ru.flydrone.flydronejavatesttask.yandexcloud.dao.YandexCloudRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AvatarRepositoryImpl implements AvatarRepository {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final YandexCloudRepository yandexCloudRepository;

    @Autowired
    AvatarRepositoryImpl(YandexCloudRepository repository) {
        this.yandexCloudRepository = repository;
    }

    @Override
    public void insertAvatar(Long userProfileId, String avatarId) {
        final String INSERT_FILE_SQL = "INSERT INTO flydrone.file (external_id) " +
                "VALUES :external_id";

        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("external_id", avatarId);
        namedParameterJdbcTemplate.update(INSERT_FILE_SQL, parameters);

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

//    @Override
//    public Optional<String> getAvatarId(Long userProfileId) {
//        final String SELECT_SQL = "SELECT " +
//                "avatar_file_id " +
//                "FROM flydrone_profile.user_profile " +
//                "WHERE user_profile_id = :user_profile_id";
//        Map<String, Object> parameters = new HashMap<>(1);
//        parameters.put("user_profile_id", userProfileId);
//        try {
//            return Optional.of(namedParameterJdbcTemplate.queryForObject(SELECT_SQL, parameters, String.class));
//        } catch (IncorrectResultSizeDataAccessException ex) {
//            return Optional.empty();
//        }
//    }

    @Override
    public void deleteAvatar(String avatarId) {
        final String UPDATE_OLD_FILE_SQL = "UPDATE flydrone.file SET is_deleted = TRUE " +
                "WHERE external_id = :external_id";

        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("external_id", avatarId);
        namedParameterJdbcTemplate.update(UPDATE_OLD_FILE_SQL, parameters);
    }
}
