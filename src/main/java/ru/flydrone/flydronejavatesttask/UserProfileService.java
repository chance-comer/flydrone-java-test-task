package ru.flydrone.flydronejavatesttask;

import java.util.Optional;

public interface UserProfileService {
    Optional<UserProfileDTO> getUserProfile(long id);
    Optional<Long> saveUserProfile(UserProfileDTO userProfile);
    int deleteUserProfile(Long id);
}
