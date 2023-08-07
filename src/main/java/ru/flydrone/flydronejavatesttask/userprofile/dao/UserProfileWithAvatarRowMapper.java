package ru.flydrone.flydronejavatesttask.userprofile.dao;

import org.springframework.jdbc.core.RowMapper;
import ru.flydrone.flydronejavatesttask.userprofile.dao.UserProfileRowMapper;
import ru.flydrone.flydronejavatesttask.userprofile.dto.UserProfileDTO;
import ru.flydrone.flydronejavatesttask.userprofile.dto.UserProfileWithAvatarDTO;

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
