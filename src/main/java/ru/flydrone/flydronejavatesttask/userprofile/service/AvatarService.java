package ru.flydrone.flydronejavatesttask.userprofile.service;

import com.amazonaws.services.s3.model.S3Object;
import org.springframework.web.multipart.MultipartFile;
import ru.flydrone.flydronejavatesttask.userprofile.dto.UserProfileWithAvatarDTO;

import java.util.Optional;

public interface AvatarService {
    String saveAvatar(Long userProfileId, MultipartFile avatar);
    S3Object getAvatar(Long userProfileId);
    void deleteAvatar(Long userProfileId);
    UserProfileWithAvatarDTO getUserProfileWithAvatar(Long userProfileId);
}
