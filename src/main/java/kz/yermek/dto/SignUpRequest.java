package kz.yermek.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import kz.yermek.validations.PasswordValidation;
import kz.yermek.validations.EmailValidation;

@Builder
@Getter
@Setter
@NoArgsConstructor
public class SignUpRequest {
    @EmailValidation
    private String email;
    @PasswordValidation
    private String password;
    private String confirmPassword;

    public SignUpRequest(String email, String password, String confirmPassword) {
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
}
