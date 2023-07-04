package ru.flydrone.flydronejavatesttask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserProfileController {
    private final UserProfileService service;

    @Autowired
    public UserProfileController(UserProfileService service) {
        this.service = service;
    }

    @PostMapping("/api/save-user-profile")
    public long saveUserProfile(@RequestBody UserProfileDTO userProfile) {
        return service.saveUserProfile(userProfile);
    }
}
