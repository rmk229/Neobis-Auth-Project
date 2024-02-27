package neo.neobis_auth_project.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import neo.neobis_auth_project.validations.EmailValidation;
import neo.neobis_auth_project.validations.PasswordValidation;

@Builder
@Getter
@Setter
@NoArgsConstructor
public class SignUpRequest {
    @EmailValidation
    private String email;
    @PasswordValidation
    private String password;

    public SignUpRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
