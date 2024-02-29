package neo.neobis_auth_project.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<EmailValidation, String> {
        private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}$";

        @Override
        public boolean isValid(String email, ConstraintValidatorContext context) {
            return email != null && email.matches(EMAIL_REGEX);
        }
}