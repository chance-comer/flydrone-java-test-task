package ru.flydrone.flydronejavatesttask.userprofile.dto;

public class UserProfileWithAvatarDTO extends UserProfileDTO {
    private String avatarExternalId;

    public static String avatarNotFoundMessage = "Avatar not found";

    public String getExternalAvatarId() {
        return avatarExternalId;
    }

    public void setAvatarExternalId(String avatarExternalId) {
        this.avatarExternalId = avatarExternalId;
    }

    public UserProfileWithAvatarDTO(UserProfileDTO userProfileDTO) {
        super(userProfileDTO.getId(),
                userProfileDTO.getLastName(),
                userProfileDTO.getFirstName(),
                userProfileDTO.getPatronymic(),
                userProfileDTO.getBirthdate());
    }

    public UserProfileWithAvatarDTO() {

    }
}
