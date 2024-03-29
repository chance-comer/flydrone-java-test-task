package ru.flydrone.flydronejavatesttask.userprofile.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class UserProfileDTO {
    private Long id;
    @NotBlank
    private String lastName;
    @NotBlank
    private String firstName;
    private String patronymic;
    @NotNull
    private LocalDate birthdate;

    public static String resourceNotFoundMessage = "User profile not found";

    public UserProfileDTO(Long id, String lastName, String firstName, String patronymic, LocalDate birthdate) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.birthdate = birthdate;
    }

    public UserProfileDTO() {}

    public String getFirstName() {
        return firstName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public Long getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }
}
