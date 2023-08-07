package ru.flydrone.flydronejavatesttask.userprofile.service;

import com.amazonaws.services.s3.model.S3Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeType;
import org.springframework.web.multipart.MultipartFile;
import ru.flydrone.flydronejavatesttask.DataNotFoundException;
import ru.flydrone.flydronejavatesttask.userprofile.dao.AvatarRepository;
import ru.flydrone.flydronejavatesttask.userprofile.dao.UserProfileRepository;
import ru.flydrone.flydronejavatesttask.userprofile.dto.UserProfileDTO;
import ru.flydrone.flydronejavatesttask.userprofile.dto.UserProfileWithAvatarDTO;
import ru.flydrone.flydronejavatesttask.userprofile.validator.UserProfileValidator;

import java.util.Optional;

@Service
public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final AvatarRepository avatarRepository;

    @Autowired
    UserProfileServiceImpl(UserProfileRepository userProfileRepository,
                           AvatarRepository avatarRepository) {
        this.userProfileRepository = userProfileRepository;
        this.avatarRepository = avatarRepository;
    }

    @Override
    public UserProfileDTO getUserProfile(Long id) {
        return userProfileRepository.getUserProfile(id).orElseThrow(() -> new DataNotFoundException("User profile not found", id));
    }

    @Override
    public Long saveUserProfile(UserProfileDTO userProfile) {
        new UserProfileValidator(userProfile).validate();
        Long userProfileId = userProfile.getId();
        if (userProfileId != null) {
            UserProfileDTO existingUserProfile = userProfileRepository
                    .getUserProfile(userProfileId)
                    .orElseThrow(() -> new DataNotFoundException("User profile not found", userProfile.getId()));
            userProfileRepository.updateUserProfile(userProfile);
        } else {
            userProfileId = userProfileRepository.insertUserProfile(userProfile);
        }
        return userProfileId;
    }

    @Override
    @Transactional
    public void deleteUserProfile(Long id) {
        Optional<UserProfileWithAvatarDTO> existingUserProfile = userProfileRepository.getUserProfileWithAvatar(id);
        if (existingUserProfile.isEmpty()) {
            throw new DataNotFoundException("User profile not found", id);
        } else {
            avatarRepository.deleteAvatar(existingUserProfile.get().getExternalAvatarId());
            userProfileRepository.deleteUserProfile(id);
        }
    }

}
