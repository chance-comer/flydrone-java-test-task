package ru.flydrone.flydronejavatesttask.userprofile.dao;

import ru.flydrone.flydronejavatesttask.userprofile.dto.UserProfileWithAvatarDTO;

import java.util.Optional;

public interface AvatarRepository {
    Optional<UserProfileWithAvatarDTO> getUserProfileWithAvatar(Long id);

    void updateAvatarId(Long userProfileId, String avatarId);

    void deleteAvatar(Long userProfileId);
}
