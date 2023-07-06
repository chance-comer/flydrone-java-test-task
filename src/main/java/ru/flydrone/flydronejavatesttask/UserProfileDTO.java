package ru.flydrone.flydronejavatesttask;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Date;

public class UserProfileDTO {
    private Long id;
    @NotBlank(message = "lastName cant't be empty")
    private final String lastName;
    @NotBlank(message = "firstName cant't be empty")
    private final String firstName;
    private final String patronymic;
    @NotNull(message = "birthdate cant't be empty")
    @Max((LocalDateTime.now()).minusMonths(12 * 18))
    private final Date birthdate;

    public UserProfileDTO(Long id, String lastName, String firstName, String patronymic, Date birthdate) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.birthdate = birthdate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public Long getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }
}
