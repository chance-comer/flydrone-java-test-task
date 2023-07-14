package ru.flydrone.flydronejavatesttask;

import com.amazonaws.services.s3.model.S3Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
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
    public Optional<Long> saveAvatar(Long userProfileId, MultipartFile avatar) {
        return repository.saveAvatar(userProfileId, avatar);
    }

    @Override
    public Optional<S3Object> getAvatar(Long userProfileId) {
        return repository.getAvatar(userProfileId);
    }

    @Override
    public Optional<Long> deleteAvatar(Long userProfileId) {
        return repository.deleteAvatar(userProfileId);
    }

}
