package ru.flydrone.flydronejavatesttask.userprofile.service;

import com.amazonaws.services.s3.model.S3Object;
import org.springframework.web.multipart.MultipartFile;
import ru.flydrone.flydronejavatesttask.userprofile.dto.UserProfileDTO;

import java.util.Optional;

public interface UserProfileService {
    Optional<UserProfileDTO> getUserProfile(Long id);
    Optional<Long> saveUserProfile(UserProfileDTO userProfile);
    Optional<Long> deleteUserProfile(Long id);
    Optional<Long> saveAvatar(Long userProfileId, MultipartFile avatar);
    Optional<S3Object> getAvatar(Long userProfileId);
    Optional<Long> deleteAvatar(Long userProfileId);
}
