package ru.flydrone.flydronejavatesttask.userprofile.validator;

import ru.flydrone.flydronejavatesttask.ValidationException;
import ru.flydrone.flydronejavatesttask.userprofile.dto.UserProfileDTO;

import java.time.LocalDate;
import java.time.Period;

public class UserProfileValidator {
    private final UserProfileDTO userProfile;

    public UserProfileValidator(UserProfileDTO userProfile) {
        this.userProfile = userProfile;
    }

    public void validate() throws ValidationException {
        validateAge();
    }

    private void validateAge() {
        if (Period.between(userProfile.getBirthdate(), LocalDate.now()).getYears() < 18) {
            throw new ValidationException("Age must be more or equal than 18 years");
        }
    }
}
