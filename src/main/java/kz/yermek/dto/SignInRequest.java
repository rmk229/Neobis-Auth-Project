package kz.yermek.dto;

import lombok.Builder;
import kz.yermek.validations.PasswordValidation;
import kz.yermek.validations.EmailValidation;

@Builder
public record SignInRequest (
        @EmailValidation
        String email,
        @PasswordValidation
        String password
    ){}