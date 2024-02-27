package neo.neobis_auth_project.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<EmailValidation, String> {
    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return (email.endsWith("gmail.com") || (email.endsWith("mail.ru") && (email.contains("@"))));
    }
}