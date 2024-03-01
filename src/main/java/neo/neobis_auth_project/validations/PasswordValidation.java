package neo.neobis_auth_project.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
public @interface PasswordValidation {
    String message() default "Пароль должен содержать не менее 8 и не более 15 символов, включать в себя буквы (в том числе хотя бы одну заглавную), цифры от 0 до 9, и, по меньшей мере, один специальный символ.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}