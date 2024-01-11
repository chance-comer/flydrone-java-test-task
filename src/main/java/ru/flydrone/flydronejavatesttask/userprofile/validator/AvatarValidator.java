package ru.flydrone.flydronejavatesttask.userprofile.validator;

import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.flydrone.flydronejavatesttask.ValidationException;

import java.util.List;

public class AvatarValidator {
    private final List<MimeType> VALID_AVATAR_TYPE = List.of(MimeTypeUtils.IMAGE_JPEG, MimeTypeUtils.IMAGE_PNG);
    private final int MAX_AVATAR_SIZE = 2 * 1024 * 1024;
    private final String INVALID_FILE_EXT_MSG = "File must have JPG or PNG format";
    private final String INVALID_FILE_SIZE_MSG = "Max file size exceeded (must be less than 2 Mb)";

    private final MultipartFile avatar;

    public AvatarValidator(MultipartFile avatar) {
        this.avatar = avatar;
    }

    public void validate() throws ValidationException {
        validateMimeType();
        validateSize();
    }

    public void validateMimeType() {
        if (!VALID_AVATAR_TYPE.contains(MimeType.valueOf(avatar.getContentType()))) {
            throw new ValidationException(INVALID_FILE_EXT_MSG);
        }
    }

    public void validateSize() {
        if (avatar.getSize() > MAX_AVATAR_SIZE) {
            throw new ValidationException(INVALID_FILE_SIZE_MSG);
        }
    }
}
