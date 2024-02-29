package neo.neobis_auth_project.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordValidation, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        return password != null && password.length() >= 8 && containsAlphabeticAndNumericCharacters(password);
    }

    private boolean containsAlphabeticAndNumericCharacters(String password) {
        return password.matches("^(?=.*[A-Za-z])(?=.*\\d).+$");
    }
}