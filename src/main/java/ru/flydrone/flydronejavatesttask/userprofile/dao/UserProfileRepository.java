package ru.flydrone.flydronejavatesttask.userprofile.dao;

import com.amazonaws.services.s3.model.S3Object;
import org.springframework.web.multipart.MultipartFile;
import ru.flydrone.flydronejavatesttask.userprofile.dto.UserProfileWithAvatarDTO;
import ru.flydrone.flydronejavatesttask.userprofile.dto.UserProfileDTO;

import java.util.Optional;

public interface UserProfileRepository {
    void updateUserProfile(UserProfileDTO userProfile);
    Long insertUserProfile(UserProfileDTO userProfile);
    void updateAvatarId(Long userProfileId, String avatarId);
    // Optional<Long> saveUserProfile(UserProfileDTO userProfile);
    void deleteUserProfile(Long id);
    Optional<UserProfileDTO> getUserProfile(Long id);
    Optional<UserProfileWithAvatarDTO> getUserProfileWithAvatar(Long id);
    void deleteAvatar(Long userProfileId);
}
