package ru.flydrone.flydronejavatesttask;

import java.util.Optional;

public interface UserProfileRepository {
    Optional<Long> saveUserProfile(UserProfileDTO userProfile);
    int deleteUserProfile(long id);
    Optional<UserProfileDTO> getUserProfile(long id);
}
