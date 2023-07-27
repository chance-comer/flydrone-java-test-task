package ru.flydrone.flydronejavatesttask.userprofile.service;

import com.amazonaws.services.s3.model.S3Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.flydrone.flydronejavatesttask.userprofile.dao.UserProfileRepository;
import ru.flydrone.flydronejavatesttask.userprofile.dto.UserProfileDTO;
import ru.flydrone.flydronejavatesttask.userprofile.validator.UserProfileValidator;

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
        new UserProfileValidator(userProfile).validate();
        Long userProfileId = userProfile.getId();
        if (userProfileId != null) {
            Optional<UserProfileDTO> existingUserProfile = repository.getUserProfile(userProfile.getId());
            if (existingUserProfile.isEmpty()) {
                return Optional.empty();
            } else {
                repository.updateUserProfile(userProfile);
            }
        } else {
            userProfileId = repository.insertUserProfile(userProfile);
        }
        return Optional.of(userProfileId);
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
