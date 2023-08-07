package ru.flydrone.flydronejavatesttask.userprofile.service;

import com.amazonaws.services.s3.model.S3Object;
import org.springframework.web.multipart.MultipartFile;
import ru.flydrone.flydronejavatesttask.userprofile.dto.UserProfileDTO;

import java.util.Optional;

public interface UserProfileService {
    UserProfileDTO getUserProfile(Long id);
    Long saveUserProfile(UserProfileDTO userProfile);
    void deleteUserProfile(Long id);
}
