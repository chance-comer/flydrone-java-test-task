package ru.flydrone.flydronejavatesttask.userprofile.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.flydrone.flydronejavatesttask.userprofile.dto.UserProfileDTO;
import ru.flydrone.flydronejavatesttask.userprofile.service.UserProfileService;

import javax.validation.Valid;

@RestController
@RequestMapping("api/profile")
@Tag(name = "UserProfile", description = "АПИ для работы с профилем пользователя")
public class UserProfileController {
    private final UserProfileService service;

    @Autowired
    public UserProfileController(UserProfileService service) {
        this.service = service;
    }

    @Operation(summary = "Получить профиль пользователя по id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Профиль пользователя"),
                    @ApiResponse(responseCode = "404", description = "Профиль с указанным id не найден",
                            content = @Content(mediaType = "text/plain; charset=utf-8"))
            })
    @GetMapping("/{id}")
    public UserProfileDTO getUserProfile(@PathVariable Long id) {
        return service.getUserProfile(id);
    }

    @Operation(summary = "Сохранить профиль пользователя (без аватара)",
            description = "В случае указания id обновляет профиль пользователя с заданным id. При незаданном id добавляет новый профиль пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Идентификатор профиля пользователя"),
                    @ApiResponse(responseCode = "400", description = "Профиль не прошел валидацию"),
                    @ApiResponse(responseCode = "404", description = "Профиль с указанным id не найден",
                            content = @Content(mediaType = "text/plain; charset=utf-8")),
                    @ApiResponse(responseCode = "503", description = "Ошибка при выполнении запроса на обновление")
            })
    @PostMapping
    public Long saveUserProfile(@RequestBody @Valid UserProfileDTO userProfile) {
        return service.saveUserProfile(userProfile);
    }

    @Operation(summary = "Сохранить профиль пользователя с аватаром",
            description = "В случае указания id обновляет профиль пользователя с заданным id. При незаданном id добавляет новый профиль пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Идентификатор профиля пользователя"),
                    @ApiResponse(responseCode = "400", description = "Профиль не прошел валидацию"),
                    @ApiResponse(responseCode = "404", description = "Профиль с указанным id не найден",
                            content = @Content(mediaType = "text/plain; charset=utf-8")),
                    @ApiResponse(responseCode = "503", description = "Ошибка серверпри выполнении запроса на обновление")
            })
    @PostMapping(value = "/full", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Long saveFullUserProfile(@Parameter() @RequestPart("userprofile") @Valid UserProfileDTO userProfile,
                                    @RequestPart("avatar") MultipartFile avatar) {
        return service.saveFullUserProfile(userProfile, avatar);
    }

    @Operation(summary = "Удалить профиль пользователя",
            responses = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "404", description = "Профиль с указанным id не найден",
                            content = @Content(mediaType = "text/plain; charset=utf-8")),
                    @ApiResponse(responseCode = "503", description = "Ошибка при удалении")
            })
    @DeleteMapping("/{id}")
    public void deleteUserProfile(@PathVariable Long id) {
        service.deleteUserProfile(id);
    }
}
