package neo.neobis_auth_project.dto;

import lombok.Builder;
import neo.neobis_auth_project.validations.PasswordValidation;
import neo.neobis_auth_project.validations.EmailValidation;

@Builder
public record SignInRequest (
        @EmailValidation
        String email,
        @PasswordValidation
        String password
    ){}