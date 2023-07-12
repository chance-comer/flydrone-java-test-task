package ru.flydrone.flydronejavatesttask;

import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UserProfileService {
    Optional<UserProfileDTO> getUserProfile(Long id);
    Optional<Long> saveUserProfile(UserProfileDTO userProfile);
    Optional<Long> deleteUserProfile(Long id);
    Optional<Long> saveAvatar(Long id, MultipartFile avatar);
}
