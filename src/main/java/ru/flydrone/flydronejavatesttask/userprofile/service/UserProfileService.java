package ru.flydrone.flydronejavatesttask.userprofile.service;

import ru.flydrone.flydronejavatesttask.userprofile.dto.UserProfileDTO;
import ru.flydrone.flydronejavatesttask.userprofile.dto.UserProfileWithAvatarDTO;

public interface UserProfileService {
    UserProfileDTO getUserProfile(Long id);
//    UserProfileWithAvatarDTO getUserProfileWithAvatar(Long id);
    Long saveUserProfile(UserProfileDTO userProfile);
    void deleteUserProfile(Long id);
//    void updateAvatarId(Long userProfileId, String avatarId);
//    void deleteAvatar(Long userProfileId);
}
