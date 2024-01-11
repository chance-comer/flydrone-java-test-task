package ru.flydrone.flydronejavatesttask.userprofile.validator;

import ru.flydrone.flydronejavatesttask.ValidationException;
import ru.flydrone.flydronejavatesttask.userprofile.dto.UserProfileDTO;

import java.time.LocalDate;
import java.time.Period;

public class UserProfileValidator {
    private final UserProfileDTO userProfile;
    private final int MIN_AGE = 18;
    private final String INVALID_AGE_MSG = "Age must be more or equal than 18 years";

    public UserProfileValidator(UserProfileDTO userProfile) {
        this.userProfile = userProfile;
    }

    public void validate() throws ValidationException {
        validateAge();
    }

    private void validateAge() {
        if (Period.between(userProfile.getBirthdate(), LocalDate.now()).getYears() < MIN_AGE) {
            throw new ValidationException(INVALID_AGE_MSG);
        }
    }
}
