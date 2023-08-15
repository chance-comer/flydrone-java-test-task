package ru.flydrone.flydronejavatesttask.userprofile.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.flydrone.flydronejavatesttask.DataNotFoundException;
import ru.flydrone.flydronejavatesttask.userprofile.dao.UserProfileRepository;
import ru.flydrone.flydronejavatesttask.userprofile.dto.UserProfileDTO;
import ru.flydrone.flydronejavatesttask.userprofile.validator.UserProfileValidator;

@Service
public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final AvatarService avatarService;

    @Autowired
    UserProfileServiceImpl(UserProfileRepository userProfileRepository,
                           AvatarService avatarService) {
        this.userProfileRepository = userProfileRepository;
        this.avatarService = avatarService;
    }

    @Override
    public UserProfileDTO getUserProfile(Long id) {
        return userProfileRepository.getUserProfile(id).orElseThrow(() -> new DataNotFoundException(UserProfileDTO.resourceNotFoundMessage, id));
    }

    @Override
    public Long saveUserProfile(UserProfileDTO userProfile) {
        new UserProfileValidator(userProfile).validate();
        Long userProfileId = userProfile.getId();
        if (userProfileId != null) {
            UserProfileDTO existingUserProfile = userProfileRepository
                    .getUserProfile(userProfileId)
                    .orElseThrow(() -> new DataNotFoundException(UserProfileDTO.resourceNotFoundMessage, userProfile.getId()));
            userProfileRepository.updateUserProfile(userProfile);
        } else {
            userProfileId = userProfileRepository.insertUserProfile(userProfile);
        }
        return userProfileId;
    }

    @Override
    public Long saveFullUserProfile(UserProfileDTO userProfile, MultipartFile avatar) {
        Long userProfileId = saveUserProfile(userProfile);
        avatarService.saveAvatar(userProfileId, avatar);
        return userProfileId;
    }

    @Override
    @Transactional
    public void deleteUserProfile(Long id) {
        avatarService.deleteAvatar(id);
        userProfileRepository.deleteUserProfile(id);
    }
}
