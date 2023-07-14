package ru.flydrone.flydronejavatesttask;

public class UserProfileWithAvatarDTO extends UserProfileDTO {
    private String avatarExternalId;

    public String getExternalAvatarId() {
        return avatarExternalId;
    }

    public void setAvatarExternalId(String avatarExternalId) {
        this.avatarExternalId = avatarExternalId;
    }
}
