package ru.flydrone.flydronejavatesttask;

public interface UserProfileService {
    UserProfileDTO getUserProfile(long id);
    long saveUserProfile(UserProfileDTO userProfile);
    void deleteUserProfile(UserProfileDTO userProfile);
}
