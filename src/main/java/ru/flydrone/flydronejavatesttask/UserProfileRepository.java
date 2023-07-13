package ru.flydrone.flydronejavatesttask;

import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UserProfileRepository {
    Optional<Long> saveUserProfile(UserProfileDTO userProfile);
    Optional<Long> deleteUserProfile(Long id);
    Optional<UserProfileDTO> getUserProfile(Long id);
    Optional<UserProfileWithAvatarDTO> getUserProfileWithAvatar(Long id);
    Optional<Long> saveAvatar(Long id, MultipartFile avatar);
}
