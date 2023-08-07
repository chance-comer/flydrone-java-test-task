package ru.flydrone.flydronejavatesttask.userprofile.validator;

import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.flydrone.flydronejavatesttask.ValidationException;
import ru.flydrone.flydronejavatesttask.userprofile.dto.UserProfileDTO;

import java.util.List;

public class AvatarValidator {
    final List<MimeType> VALID_AVATAR_TYPE = List.of(MimeTypeUtils.IMAGE_JPEG, MimeTypeUtils.IMAGE_PNG);
    final int MAX_AVATAR_SIZE = 2 * 1024;

    private final MultipartFile avatar;

    public AvatarValidator(MultipartFile avatar) {
        this.avatar = avatar;
    }

    public void validate() throws ValidationException {
        validateMimeType();
        validateSize();
    }

    public void validateMimeType() {
        if (!VALID_AVATAR_TYPE.contains(avatar.getContentType())) {
            throw new ValidationException("File must have JPG or PNG format");
        }
    }

    public void validateSize() {
        if (avatar.getSize() > MAX_AVATAR_SIZE) {
            throw new ValidationException("Max file size exceeded (must be less than 2 Mb)");
        }
    }
}
