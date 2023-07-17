package ru.flydrone.flydronejavatesttask;

public class UserProfileWithAvatarDTO extends UserProfileDTO {
    private String avatarExternalId;

    public String getExternalAvatarId() {
        return avatarExternalId;
    }

    public void setAvatarExternalId(String avatarExternalId) {
        this.avatarExternalId = avatarExternalId;
    }

    public UserProfileWithAvatarDTO(UserProfileDTO userProfileDTO) {
        this.setId(userProfileDTO.getId());
        this.setBirthdate(userProfileDTO.getBirthdate());
        this.setFirstName(userProfileDTO.getFirstName());
        this.setLastName(userProfileDTO.getLastName());
        this.setPatronymic(userProfileDTO.getPatronymic());
    }

    public UserProfileWithAvatarDTO() {

    }
}
