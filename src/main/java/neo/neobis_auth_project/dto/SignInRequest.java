package neo.neobis_auth_project.dto;

import lombok.Builder;
import neo.neobis_auth_project.validations.EmailValidation;
import neo.neobis_auth_project.validations.PasswordValidation;

@Builder
public record SignInRequest (
        @EmailValidation
        String email,
        @PasswordValidation
        String password
    ){}