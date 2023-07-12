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

import java.io.IOException;
import java.util.*;

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
        final Optional<UserProfileDTO> userProfileDTO;
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
        List<UserProfileDTO> userProfileDTOList = namedParameterJdbcTemplate.query(SELECT_SQL, parameters, new UserProfileRowMapper());
        userProfileDTO = userProfileDTOList.size() > 0 ? Optional.of(userProfileDTOList.get(0)) : Optional.empty();
        return userProfileDTO;
    }

    @Override
    public Optional<Long> saveAvatar(Long userProfileId, MultipartFile avatar) {
        Optional<Long> upsertedUserProfileId = Optional.empty();
        final String bucketName = "bucket-user-profile-avatar";
        final UUID uuid = java.util.UUID.randomUUID();
        final String UPSERT_SQL = "INSERT INTO flydrone_profile.avatar (user_profile_id, file_name, file_base64)" +
                "VALUES (:user_profile_id, :file_name, :external_id)" +
                "ON CONFLICT (user_profile_id) DO UPDATE SET" +
                "file_name = EXCLUDED.file_name," +
                "file_base64 = EXCLUDED.file_base64";

        if (getUserProfile(userProfileId).isEmpty()) {
            return upsertedUserProfileId;
        }

        AWSCredentials credentials = new BasicAWSCredentials(
                "YCAJEGUZ5TZi1GN5uXeao0YTx",
                "YCNtmSkY6IDkcGBJsmTyRDYgCs4ACZomGzFhRNc3"
        );
        AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(
                        new AmazonS3ClientBuilder.EndpointConfiguration(
                                "storage.yandexcloud.net", "ru-central1"
                        )
                )
                .build();
        try {
            s3.putObject(
                    bucketName,
                    uuid.toString(),
                    avatar.getInputStream(),
                    new ObjectMetadata()
            );

            Map<String, Object> parameters = new HashMap<>(3);
            parameters.put("user_profile_id", userProfileId);
            parameters.put("file_name", avatar.getName());
            parameters.put("external_id", uuid.toString());

            namedParameterJdbcTemplate.update(UPSERT_SQL, parameters);
            upsertedUserProfileId = Optional.of(userProfileId);
        } catch (IOException ex) {

        }
        return upsertedUserProfileId;
    }
}
