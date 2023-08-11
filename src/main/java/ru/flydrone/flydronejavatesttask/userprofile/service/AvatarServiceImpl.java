package ru.flydrone.flydronejavatesttask.userprofile.service;

import com.amazonaws.services.s3.model.S3Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.flydrone.flydronejavatesttask.DataNotFoundException;
import ru.flydrone.flydronejavatesttask.userprofile.dao.AvatarRepository;
import ru.flydrone.flydronejavatesttask.userprofile.dto.UserProfileWithAvatarDTO;
import ru.flydrone.flydronejavatesttask.userprofile.validator.AvatarValidator;
import ru.flydrone.flydronejavatesttask.yandexcloud.service.YandexCloudService;

import java.util.UUID;

@Service
public class AvatarServiceImpl implements AvatarService {
    private final AvatarRepository avatarRepository;
    private final YandexCloudService yandexCloudService;

    @Autowired
    AvatarServiceImpl(AvatarRepository avatarRepository,
                      YandexCloudService yandexCloudService) {
        this.avatarRepository = avatarRepository;
        this.yandexCloudService = yandexCloudService;
    }

    @Override
    @Transactional
    public String saveAvatar(Long userProfileId, MultipartFile avatar) {
        new AvatarValidator(avatar).validate();

        UserProfileWithAvatarDTO userProfileWithAvatar = avatarRepository.getUserProfileWithAvatar(userProfileId)
                .orElseThrow(() -> new DataNotFoundException("User profile not found", userProfileId));

        String avatarFileId = userProfileWithAvatar.getExternalAvatarId();

        final UUID newAvatarFileId = java.util.UUID.randomUUID();

        yandexCloudService.saveObject(
                newAvatarFileId.toString(),
                avatar
        );

        avatarRepository.updateAvatarId(userProfileId, newAvatarFileId.toString());

        if (avatarFileId != null) {
            yandexCloudService.deleteObject(avatarFileId);
        }

        return newAvatarFileId.toString();
    }

    @Override
    public S3Object getAvatar(Long userProfileId) {
        UserProfileWithAvatarDTO userProfileWithAvatar = avatarRepository.getUserProfileWithAvatar(userProfileId)
                .orElseThrow(() -> new DataNotFoundException("User profile not found", userProfileId));

        if (userProfileWithAvatar.getExternalAvatarId() == null) {
            throw new DataNotFoundException("Avatar not found");
        }

        String avatarId = userProfileWithAvatar.getExternalAvatarId();

        return yandexCloudService.getObject(avatarId);
    }

    @Override
    @Transactional
    public void deleteAvatar(Long userProfileId) {
        UserProfileWithAvatarDTO userProfileWithAvatar = avatarRepository.getUserProfileWithAvatar(userProfileId)
                .orElseThrow(() -> new DataNotFoundException("User profile not found", userProfileId));

//        if (userProfileWithAvatar.getExternalAvatarId() == null) {
//            throw new DataNotFoundException("Avatar not found");
//        }

        avatarRepository.deleteAvatar(userProfileId);
        yandexCloudService.deleteObject(userProfileWithAvatar.getExternalAvatarId());
    }

    @Override
    public UserProfileWithAvatarDTO getUserProfileWithAvatar(Long id) {
        return avatarRepository.getUserProfileWithAvatar(id)
                .orElseThrow(() -> new DataNotFoundException("User profile not found", id));
    }
}
