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
@Constraint(validatedBy = EmailValidator.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
public @interface EmailValidation {
    String message() default "Адрес электронной почты должен заканчиваться на 'gmail.com' или 'mail.ru' и содержать @.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
