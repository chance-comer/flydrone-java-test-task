package ru.flydrone.flydronejavatesttask.userprofile.controller;

import com.amazonaws.services.s3.model.S3Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.flydrone.flydronejavatesttask.userprofile.service.AvatarService;

@RestController
public class AvatarController {
    private final AvatarService service;

    @Autowired
    public AvatarController(AvatarService service) {
        this.service = service;
    }

    @PostMapping(value = "/api/avatar/{userProfileId}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity saveAvatar(@RequestParam("avatar") MultipartFile avatar,
                                     @PathVariable Long userProfileId) {
        String savedAvatarExternalId = service.saveAvatar(userProfileId, avatar);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/api/avatar/{userProfileId}")
    public ResponseEntity<InputStreamResource> getAvatar(@PathVariable Long userProfileId) {
        S3Object avatar = service.getAvatar(userProfileId);

        String contentType = avatar.getObjectMetadata().getContentType();
        MediaType mediaType = MediaType.parseMediaType(contentType);
        InputStreamResource body = new InputStreamResource(avatar.getObjectContent());
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(mediaType)
                .body(body);
    }

    @DeleteMapping(value = "/api/avatar/{userProfileId}")
    public ResponseEntity deleteAvatar(@PathVariable Long userProfileId) {
        service.deleteAvatar(userProfileId);
        return new ResponseEntity(HttpStatus.OK);
    }
}
