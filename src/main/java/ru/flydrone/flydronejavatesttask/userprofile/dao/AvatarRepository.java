package ru.flydrone.flydronejavatesttask.userprofile.dao;

import com.amazonaws.services.s3.model.S3Object;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface AvatarRepository {
    // Optional<Long> saveAvatar(Long userProfileId, MultipartFile avatar);
    void insertAvatar(Long userProfileId, String avatarId);
    // Optional<String> getAvatarId(Long userProfileId);
    void deleteAvatar(String avatarId);
}
