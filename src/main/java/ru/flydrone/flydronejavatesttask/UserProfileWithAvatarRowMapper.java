package ru.flydrone.flydronejavatesttask;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserProfileWithAvatarRowMapper implements RowMapper<UserProfileWithAvatarDTO> {
    @Override
    public UserProfileWithAvatarDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserProfileWithAvatarDTO userProfileWithAvatar = new UserProfileWithAvatarDTO();

        userProfileWithAvatar.setId(rs.getLong("ID"));
        userProfileWithAvatar.setFirstName(rs.getString("FIRST_NAME"));
        userProfileWithAvatar.setLastName(rs.getString("LAST_NAME"));
        userProfileWithAvatar.setBirthdate(rs.getDate("BIRTHDATE").toLocalDate());
        userProfileWithAvatar.setPatronymic(rs.getString("PATRONYMIC"));
        userProfileWithAvatar.setAvatarExternalId(rs.getString("AVATAR_EXTERNAL_ID"));

        return userProfileWithAvatar;
    }
}
