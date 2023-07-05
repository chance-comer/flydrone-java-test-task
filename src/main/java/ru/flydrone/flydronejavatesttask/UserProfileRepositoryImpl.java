package ru.flydrone.flydronejavatesttask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserProfileRepositoryImpl implements UserProfileRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public long saveUserProfile(UserProfileDTO userProfile) {
        if (userProfile.getId() != null) {
            final String UPDATE_SQL =
                    "UPDATE flydrone_profile.user_profile SET" +
                            " first_name = ?," +
                            " last_name = ?," +
                            " patronymic = ?," +
                            " birthdate = ?" +
                            " WHERE id = ?;";

            jdbcTemplate.update(
                    UPDATE_SQL, userProfile.getFirstName(), userProfile.getLastName(), userProfile.getPatronymic(), userProfile.getBirthdate(), userProfile.getId());
        } else {
            Map<String, Object> parameters = new HashMap<>(4);
            parameters.put("first_name", userProfile.getFirstName());
            parameters.put("last_name", userProfile.getLastName());
            parameters.put("patronymic", userProfile.getPatronymic());
            parameters.put("birthdate", userProfile.getBirthdate());

            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate)
                    .withSchemaName("flydrone_profile")
                    .withTableName("user_profile")
                    .usingGeneratedKeyColumns("id");

            userProfile.setId(simpleJdbcInsert.executeAndReturnKey(parameters).longValue());
        }

        return userProfile.getId();
    }
}
