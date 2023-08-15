package ru.flydrone.flydronejavatesttask.userprofile.service;

import org.springframework.web.multipart.MultipartFile;
import ru.flydrone.flydronejavatesttask.userprofile.dto.UserProfileDTO;
import ru.flydrone.flydronejavatesttask.userprofile.dto.UserProfileWithAvatarDTO;

public interface UserProfileService {
    UserProfileDTO getUserProfile(Long id);
    Long saveUserProfile(UserProfileDTO userProfile);
    Long saveFullUserProfile(UserProfileDTO userProfile, MultipartFile avatar);
    void deleteUserProfile(Long id);
}
