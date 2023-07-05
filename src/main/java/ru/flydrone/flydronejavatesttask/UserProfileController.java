package ru.flydrone.flydronejavatesttask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserProfileController {
    private final UserProfileService service;

    @Autowired
    public UserProfileController(UserProfileService service) {
        this.service = service;
    }

    @PostMapping("/api/save-user-profile")
    public ResponseEntity<Long> saveUserProfile(@RequestBody UserProfileDTO userProfile) {
        Long userProfileId = service.saveUserProfile(userProfile);
        var status = userProfile.getId() == null ? HttpStatus.CREATED : HttpStatus.OK;
        //return service.saveUserProfile(userProfile);
        return ResponseEntity.status(status).body(userProfileId);
    }
}
