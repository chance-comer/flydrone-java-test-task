package ru.flydrone.flydronejavatesttask;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static java.util.Map.entry;

@Repository
public class UserProfileRepositoryImpl implements UserProfileRepository {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Optional<Long> saveUserProfile(UserProfileDTO userProfile) {
        Optional<Long> userProfileId = Optional.empty();
        int updatedRowCount = 0;

        if (userProfile.getId() != null) {
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
        Optional<Long> deletedUserProfileId = Optional.empty();
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
        Optional<Long> upsertedUserProfileId = Optional.empty();
        final String bucketName = "bucket-user-profile-avatar";
        final String UPSERT_SQL = "INSERT INTO flydrone_profile.avatar (user_profile_id, external_id) " +
                "VALUES (:user_profile_id, :external_id) " +
                "ON CONFLICT (user_profile_id) DO UPDATE SET " +
                "external_id = EXCLUDED.external_id";

        Optional<UserProfileWithAvatarDTO> userProfileWithAvatar = getUserProfileWithAvatar(userProfileId);

        if (userProfileWithAvatar.isEmpty()) {
            return upsertedUserProfileId;
        }

        /* AWSCredentials credentials = new BasicAWSCredentials(
                "YCAJEGUZ5TZi1GN5uXeao0YTx",
                "YCNtmSkY6IDkcGBJsmTyRDYgCs4ACZomGzFhRNc3"
        ); */
        AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                // .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(
                        new AmazonS3ClientBuilder.EndpointConfiguration(
                                "storage.yandexcloud.net", "ru-central1"
                        )
                )
                .build();
        try {
            final UUID externalId = java.util.UUID.randomUUID();
            String existingAvatarExternalId = userProfileWithAvatar.get().getExternalAvatarId();

            if (existingAvatarExternalId != null) {
                s3.deleteObject(bucketName, existingAvatarExternalId);
            }

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(avatar.getContentType());
            objectMetadata.setUserMetadata(Map.ofEntries(
                    entry("fileName", avatar.getOriginalFilename())
            ));

            s3.putObject(
                    bucketName,
                    externalId.toString(),
                    avatar.getInputStream(),
                    objectMetadata
            );

            Map<String, Object> parameters = new HashMap<>(2);

            parameters.put("user_profile_id", userProfileId);
            parameters.put("external_id", externalId.toString());

            namedParameterJdbcTemplate.update(UPSERT_SQL, parameters);
            upsertedUserProfileId = Optional.of(userProfileId);
        } catch (IOException ex) {

        }
        return upsertedUserProfileId;
    }
}
