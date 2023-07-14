package ru.flydrone.flydronejavatesttask;

import com.amazonaws.services.s3.model.S3Object;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Optional;

public interface UserProfileService {
    Optional<UserProfileDTO> getUserProfile(Long id);
    Optional<Long> saveUserProfile(UserProfileDTO userProfile);
    Optional<Long> deleteUserProfile(Long id);
    Optional<Long> saveAvatar(Long userProfileId, MultipartFile avatar);
    Optional<S3Object> getAvatar(Long userProfileId);
    Optional<Long> deleteAvatar(Long userProfileId);
}
