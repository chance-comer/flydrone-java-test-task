package ru.flydrone.flydronejavatesttask.userprofile.service;

import com.amazonaws.services.s3.model.S3Object;
import jdk.jfr.StackTrace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.flydrone.flydronejavatesttask.DataNotFoundException;
import ru.flydrone.flydronejavatesttask.userprofile.dao.AvatarRepository;
import ru.flydrone.flydronejavatesttask.userprofile.dao.UserProfileRepository;
import ru.flydrone.flydronejavatesttask.userprofile.dto.UserProfileWithAvatarDTO;
import ru.flydrone.flydronejavatesttask.userprofile.validator.AvatarValidator;
import ru.flydrone.flydronejavatesttask.yandexcloud.dao.YandexCloudRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class AvatarServiceImpl implements AvatarService {
    private final AvatarRepository avatarRepository;
    private final UserProfileRepository userProfileRepository;
    private final YandexCloudRepository yandexCloudRepository;

    @Autowired
    AvatarServiceImpl(AvatarRepository repository,
                      UserProfileRepository userProfileRepository,
                      YandexCloudRepository yandexCloudRepository) {
        this.userProfileRepository = userProfileRepository;
        this.avatarRepository = repository;
        this.yandexCloudRepository = yandexCloudRepository;
    }

    @Override
    @Transactional
    public String saveAvatar(Long userProfileId, MultipartFile avatar) {
        new AvatarValidator(avatar).validate();

        Optional<UserProfileWithAvatarDTO> userProfileWithAvatar = userProfileRepository.getUserProfileWithAvatar(userProfileId);

        if (userProfileWithAvatar.isEmpty()) {
            throw new DataNotFoundException("User profile not found", userProfileId);
        }

        String externalAvatarId = userProfileWithAvatar.get().getExternalAvatarId();

        if (externalAvatarId != null) {
            avatarRepository.deleteAvatar(externalAvatarId);
        }

        final UUID externalId = java.util.UUID.randomUUID();

        yandexCloudRepository.saveObject(
                externalId.toString(),
                avatar
        );

        userProfileRepository.updateAvatarId(userProfileId, externalId.toString());

        return externalId.toString();
    }

    @Override
    public S3Object getAvatar(Long userProfileId) {
        Optional<UserProfileWithAvatarDTO> userProfileWithAvatar = userProfileRepository.getUserProfileWithAvatar(userProfileId);

        if (userProfileWithAvatar.isEmpty()) {
            throw new DataNotFoundException("User profile not found", userProfileId);
        } else if (userProfileWithAvatar.get().getExternalAvatarId() == null) {
            throw new DataNotFoundException("Avatar not found");
        }

        String avatarId = userProfileWithAvatar.get().getExternalAvatarId();

        return yandexCloudRepository.getObject(avatarId)
                 .orElseThrow(() -> new DataNotFoundException("File not found in repository", avatarId));
    }

    @Override
    @Transactional
    public void deleteAvatar(Long userProfileId) {
        Optional<UserProfileWithAvatarDTO> userProfileWithAvatar = userProfileRepository.getUserProfileWithAvatar(userProfileId);

        if (userProfileWithAvatar.isEmpty()) {
            throw new DataNotFoundException("User profile not found", userProfileId);
        }
//        else if (userProfileWithAvatar.get().getExternalAvatarId() == null) {
//            throw new DataNotFoundException("Avatar not found");
//        }

        userProfileRepository.deleteAvatar(userProfileId);
        avatarRepository.deleteAvatar(userProfileWithAvatar.get().getExternalAvatarId());
    }
}
