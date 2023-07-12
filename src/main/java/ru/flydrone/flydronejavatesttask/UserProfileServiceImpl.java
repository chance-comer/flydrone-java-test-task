package ru.flydrone.flydronejavatesttask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileRepository repository;

    @Autowired
    UserProfileServiceImpl(UserProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<UserProfileDTO> getUserProfile(Long id) {
        return repository.getUserProfile(id);
    }

    @Override
    public Optional<Long> saveUserProfile(UserProfileDTO userProfile) {
        return repository.saveUserProfile(userProfile);
    }

    @Override
    public Optional<Long> deleteUserProfile(Long id) {
        return repository.deleteUserProfile(id);
    }

    @Override
    public Optional<Long> saveAvatar(Long id, MultipartFile avatar) {
        return repository.saveAvatar(id, avatar);
    }
}
