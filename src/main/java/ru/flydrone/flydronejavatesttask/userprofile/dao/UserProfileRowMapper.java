package ru.flydrone.flydronejavatesttask.userprofile.dao;

import org.springframework.jdbc.core.RowMapper;
import ru.flydrone.flydronejavatesttask.userprofile.dto.UserProfileDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserProfileRowMapper implements RowMapper<UserProfileDTO> {
    @Override
    public UserProfileDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserProfileDTO userProfile = new UserProfileDTO();

        userProfile.setId(rs.getLong("ID"));
        userProfile.setFirstName(rs.getString("FIRST_NAME"));
        userProfile.setLastName(rs.getString("LAST_NAME"));
        userProfile.setBirthdate(rs.getDate("BIRTHDATE").toLocalDate());
        userProfile.setPatronymic(rs.getString("PATRONYMIC"));

        return userProfile;
    }
}
