package ru.flydrone.flydronejavatesttask.userprofile.controller;

import com.amazonaws.services.s3.model.S3Object;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.flydrone.flydronejavatesttask.userprofile.service.AvatarService;

@RestController
@RequestMapping("/api/avatar/{userProfileId}")
public class AvatarController {
    private final AvatarService service;

    @Autowired
    public AvatarController(AvatarService service) {
        this.service = service;
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Сохранить аватар пользователя с заданным id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Аватар сохранен"),
                    @ApiResponse(responseCode = "404", description = "Пользователь с указанным id не найден",
                            content = @Content(mediaType = "text/plain; charset=utf-8")),
                    @ApiResponse(responseCode = "503", description = "Ошибка при сохранении аватара")
            })
    public void saveAvatar(@RequestParam("avatar") MultipartFile avatar,
                           @PathVariable Long userProfileId) {
        service.saveAvatar(userProfileId, avatar);
    }

    @GetMapping
    @Operation(summary = "Получить аватар по id пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Аватар пользователя"),
                    @ApiResponse(responseCode = "404", description = "Пользователь с указанным id не найлен или у него отсутствует аватар",
                            content = @Content(mediaType = "text/plain; charset=utf-8")),
                    @ApiResponse(responseCode = "503", description = "Ошибка при получении аватара")
            })
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

    @DeleteMapping
    @Operation(summary = "Удалить аватар пользователя с заданным id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Аватар удален"),
                    @ApiResponse(responseCode = "404", description = "Пользователь с указанным id не найден",
                            content = @Content(mediaType = "text/plain; charset=utf-8")),
                    @ApiResponse(responseCode = "503", description = "Ошибка при удалении аватара")
            })
    public void deleteAvatar(@PathVariable Long userProfileId) {
        service.deleteAvatar(userProfileId);
    }
}
