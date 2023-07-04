package ru.flydrone.flydronejavatesttask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileRepository repository;

    @Autowired
    UserProfileServiceImpl(UserProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserProfileDTO getUserProfile(long id) {
        return null;
    }

    @Override
    public long saveUserProfile(UserProfileDTO userProfile) {
        return repository.saveUserProfile(userProfile);
    }

    @Override
    public void deleteUserProfile(UserProfileDTO userProfile) {

    }
}
