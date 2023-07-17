package ru.flydrone.flydronejavatesttask;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserProfileWithAvatarRowMapper implements RowMapper<UserProfileWithAvatarDTO> {
    @Override
    public UserProfileWithAvatarDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserProfileRowMapper userProfileRowMapper = new UserProfileRowMapper();
        UserProfileDTO userProfile = userProfileRowMapper.mapRow(rs, rowNum);

        UserProfileWithAvatarDTO userProfileWithAvatar = new UserProfileWithAvatarDTO(userProfile);
        userProfileWithAvatar.setAvatarExternalId(rs.getString("AVATAR_FILE_ID"));

        return userProfileWithAvatar;
    }
}
