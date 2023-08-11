package ru.flydrone.flydronejavatesttask.userprofile.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.flydrone.flydronejavatesttask.userprofile.dto.UserProfileWithAvatarDTO;
import ru.flydrone.flydronejavatesttask.userprofile.dto.UserProfileDTO;

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
}
