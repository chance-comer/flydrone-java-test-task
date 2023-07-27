package ru.flydrone.flydronejavatesttask.userprofile.dao;

import com.amazonaws.services.s3.model.S3Object;
import org.springframework.web.multipart.MultipartFile;
import ru.flydrone.flydronejavatesttask.userprofile.dto.UserProfileWithAvatarDTO;
import ru.flydrone.flydronejavatesttask.userprofile.dto.UserProfileDTO;

import java.util.Optional;

public interface UserProfileRepository {
    void updateUserProfile(UserProfileDTO userProfile);
    Long insertUserProfile(UserProfileDTO userProfile);
    Optional<Long> saveUserProfile(UserProfileDTO userProfile);
    Optional<Long> deleteUserProfile(Long id);
    Optional<UserProfileDTO> getUserProfile(Long id);
    Optional<UserProfileWithAvatarDTO> getUserProfileWithAvatar(Long id);
    Optional<Long> saveAvatar(Long userProfileId, MultipartFile avatar);
    Optional<S3Object> getAvatar(Long userProfileId);
    Optional<Long> deleteAvatar(Long userProfileId);
}
