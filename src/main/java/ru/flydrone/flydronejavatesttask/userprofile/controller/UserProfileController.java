package ru.flydrone.flydronejavatesttask.userprofile.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.flydrone.flydronejavatesttask.userprofile.dto.UserProfileDTO;
import ru.flydrone.flydronejavatesttask.userprofile.service.UserProfileService;

import javax.validation.Valid;

@RestController
public class UserProfileController {
    private final UserProfileService service;

    @Autowired
    public UserProfileController(UserProfileService service) {
        this.service = service;
    }

    @GetMapping("/api/profile/{id}")
    public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable Long id) {
        UserProfileDTO userProfile = service.getUserProfile(id);
        return ResponseEntity.status(HttpStatus.OK).body(userProfile);
    }

    @PostMapping("/api/profile")
    public ResponseEntity<Long> saveUserProfile(@RequestBody @Valid UserProfileDTO userProfile) {
        Long userProfileId = service.saveUserProfile(userProfile);
        return ResponseEntity.status(HttpStatus.OK).body(userProfileId);
    }

    @PostMapping(value = "/api/fullprofile", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Long> saveFullUserProfile(@RequestPart("userprofile") @Valid UserProfileDTO userProfile,
                                                    @RequestPart("avatar") MultipartFile avatar) {
        Long userProfileId = service.saveFullUserProfile(userProfile, avatar);
        return ResponseEntity.status(HttpStatus.OK).body(userProfileId);
    }

    @DeleteMapping(value = "/api/profile/{id}")
    public ResponseEntity deleteUserProfile(@PathVariable Long id) {
        service.deleteUserProfile(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
