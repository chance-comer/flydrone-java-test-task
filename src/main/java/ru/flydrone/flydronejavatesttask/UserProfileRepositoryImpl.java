package ru.flydrone.flydronejavatesttask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
                userProfileId =Optional.of(userProfile.getId());
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
    public int deleteUserProfile(long id) {
        final String DELETE_SQL = "DELETE FROM flydrone_profile.user_profile WHERE id = :id";
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("id", id);

        return namedParameterJdbcTemplate.update(DELETE_SQL, parameters);
    }

    @Override
    public Optional<UserProfileDTO> getUserProfile(long id) {
        final Optional<UserProfileDTO> userProfileDTO;
        final String SELECT_SQL = "SELECT first_name, last_name, birthdate, id, patronymic FROM flydrone_profile.user_profile WHERE id = :id";
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("id", id);
        List<UserProfileDTO> userProfileDTOList = namedParameterJdbcTemplate.query(SELECT_SQL, parameters, new UserProfileRowMapper());
        userProfileDTO = userProfileDTOList.size() > 0 ? Optional.of(userProfileDTOList.get(0)) : Optional.empty();
        return userProfileDTO;
    }

}
