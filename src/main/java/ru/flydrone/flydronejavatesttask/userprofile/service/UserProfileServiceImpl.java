package ru.flydrone.flydronejavatesttask.userprofile.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        avatarService.getUserProfileWithAvatar(id);

        avatarService.deleteAvatar(id);
        userProfileRepository.deleteUserProfile(id);
    }
}
