package ru.flydrone.flydronejavatesttask;

import com.amazonaws.services.s3.model.S3Object;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UserProfileRepository {
    Optional<Long> saveUserProfile(UserProfileDTO userProfile);
    Optional<Long> deleteUserProfile(Long id);
    Optional<UserProfileDTO> getUserProfile(Long id);
    Optional<UserProfileWithAvatarDTO> getUserProfileWithAvatar(Long id);
    Optional<Long> saveAvatar(Long userProfileId, MultipartFile avatar);
    Optional<S3Object> getAvatar(Long userProfileId);
    Optional<Long> deleteAvatar(Long userProfileId);
}
