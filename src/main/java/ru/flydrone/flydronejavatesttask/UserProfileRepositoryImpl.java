package ru.flydrone.flydronejavatesttask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserProfileRepositoryImpl implements UserProfileRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public long saveUserProfile(UserProfileDTO userProfile) {
        final String INSERT_SQL =
                "INSERT INTO flydrone_profile.user_profile" +
                        " (first_name, last_name, patronymic, birthdate)" +
                        " VALUES (?, ?, ?::text, ?)";
        final String UPDATE_SQL =
                "UPDATE flydrone_profile.user_profile SET" +
                        " first_name = ?," +
                        " last_name = ?," +
                        " patronymic = ?," +
                        " birthdate = ?" +
                        " WHERE id = ?;";

        if (userProfile.getId() != null) {
            jdbcTemplate.update(
                    UPDATE_SQL, userProfile.getFirstName(), userProfile.getLastName(), userProfile.getPatronymic(), userProfile.getBirthdate(), userProfile.getId());
        } else {
            jdbcTemplate.update(
                    INSERT_SQL, userProfile.getFirstName(), userProfile.getLastName(), userProfile.getPatronymic(), userProfile.getBirthdate());
        }

        return 0;
    }
}
