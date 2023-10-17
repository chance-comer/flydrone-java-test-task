package ru.flydrone.flydronejavatesttask.userprofile.validator;

import ru.flydrone.flydronejavatesttask.ValidationException;
import ru.flydrone.flydronejavatesttask.userprofile.dto.UserProfileDTO;

import java.time.LocalDate;
import java.time.Period;

public class UserProfileValidator {
    private final UserProfileDTO userProfile;
    private final int MIN_AGE = 18;

    public UserProfileValidator(UserProfileDTO userProfile) {
        this.userProfile = userProfile;
    }

    public void validate() throws ValidationException {
        validateAge();
    }

    private void validateAge() {
        if (Period.between(userProfile.getBirthdate(), LocalDate.now()).getYears() < MIN_AGE) {
            throw new ValidationException("Age must be more or equal than 18 years");
        }
    }
}
