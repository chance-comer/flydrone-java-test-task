package ru.flydrone.flydronejavatesttask;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class UserProfileController {
    private final UserProfileService service;

    @Autowired
    public UserProfileController(UserProfileService service) {
        this.service = service;
    }

    @GetMapping("/api/profile/{id}")
    public ResponseEntity<Optional<UserProfileDTO>> getUserProfile(@PathVariable Long id) {
        Optional<UserProfileDTO> userProfile = service.getUserProfile(id);
        var status = userProfile.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK;
        return ResponseEntity.status(status).body(userProfile);
    }

    @PostMapping("/api/profile")
    public ResponseEntity<Long> saveUserProfile(@RequestBody @Valid UserProfileDTO userProfile) {
        Optional<Long> userProfileId = service.saveUserProfile(userProfile);
        var status = userProfileId.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK;
        return ResponseEntity.status(status).body(userProfile.getId());
    }

    @DeleteMapping(value = "/api/profile/{id}")
    public ResponseEntity<Long> deleteUserProfile(@PathVariable Long id) {
        int deletedRowCount = service.deleteUserProfile(id);
        var status = deletedRowCount == 0 ? HttpStatus.NOT_FOUND : HttpStatus.OK;
        return ResponseEntity.status(status).body(id);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(DataAccessException.class)
    public String handleValidationExceptions(
            DataAccessException ex) {
        return ex.getMessage();
    }
}
