package neo.neobis_auth_project.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import neo.neobis_auth_project.validations.PasswordValidation;
import neo.neobis_auth_project.validations.EmailValidation;

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
