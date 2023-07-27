package ru.flydrone.flydronejavatesttask.userprofile.controller;

import com.amazonaws.services.s3.model.S3Object;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.flydrone.flydronejavatesttask.userprofile.dto.UserProfileDTO;
import ru.flydrone.flydronejavatesttask.userprofile.service.UserProfileService;

import java.util.Optional;

@RestController
public class UserProfileController {
    private final UserProfileService service;

    @Autowired
    public UserProfileController(UserProfileService service) {
        this.service = service;
    }

    @GetMapping("/api/profile/{id}")
    public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable Long id) {
        Optional<UserProfileDTO> userProfile = service.getUserProfile(id);
        return ResponseEntity.of(userProfile);
    }

    @PostMapping(value = "/api/avatar/{userProfileId}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity saveAvatar(@RequestParam("avatar") @Size(max = 2 * 1024) MultipartFile avatar,
                                     @PathVariable Long userProfileId) {
        Optional<Long> savedAvatarUserProfileId = service.saveAvatar(userProfileId, avatar);
        return ResponseEntity.of(savedAvatarUserProfileId);
    }

    @GetMapping("/api/avatar/{userProfileId}")
    public ResponseEntity<Optional<InputStreamResource>> getAvatar(@PathVariable Long userProfileId) {
        Optional<S3Object> avatar = service.getAvatar(userProfileId);
        if (avatar.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND).body(Optional.empty());
        }
        String contentType = avatar.get().getObjectMetadata().getContentType();
        MediaType mediaType = MediaType.parseMediaType(contentType);
        Optional<InputStreamResource> body = Optional.of(new InputStreamResource(avatar.get().getObjectContent()));
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(mediaType)
                .body(body);
    }

    @DeleteMapping(value = "/api/avatar/{userProfileId}")
    public ResponseEntity<Long> deleteAvatar(@PathVariable Long userProfileId) {
        Optional<Long> deletedAvatarUserProfileId = service.deleteAvatar(userProfileId);
        return ResponseEntity.of(deletedAvatarUserProfileId);
    }

    @PostMapping("/api/profile")
    public ResponseEntity<Long> saveUserProfile(@RequestBody @Valid UserProfileDTO userProfile) {
        Optional<Long> userProfileId = service.saveUserProfile(userProfile);
        return ResponseEntity.of(userProfileId);
    }

    @DeleteMapping(value = "/api/profile/{id}")
    public ResponseEntity<Long> deleteUserProfile(@PathVariable Long id) {
        Optional<Long> deletedUserProfileId = service.deleteUserProfile(id);
        return ResponseEntity.of(deletedUserProfileId);
    }
}
