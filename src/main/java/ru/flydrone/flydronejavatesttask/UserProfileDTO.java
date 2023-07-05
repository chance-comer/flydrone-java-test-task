package ru.flydrone.flydronejavatesttask;

import java.util.Date;
import java.util.Optional;

public class UserProfileDTO {
    private Long id;
    private final String lastName;
    private final String firstName;
    private final String patronymic;
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
