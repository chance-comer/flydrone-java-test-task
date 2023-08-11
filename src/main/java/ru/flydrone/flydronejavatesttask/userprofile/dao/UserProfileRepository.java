package ru.flydrone.flydronejavatesttask.userprofile.dao;

import ru.flydrone.flydronejavatesttask.userprofile.dto.UserProfileWithAvatarDTO;
import ru.flydrone.flydronejavatesttask.userprofile.dto.UserProfileDTO;

import java.util.Optional;

public interface UserProfileRepository {
    void updateUserProfile(UserProfileDTO userProfile);
    Long insertUserProfile(UserProfileDTO userProfile);
    void deleteUserProfile(Long id);
    Optional<UserProfileDTO> getUserProfile(Long id);
}
