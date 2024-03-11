package neo.neobis_auth_project.dto;

import jakarta.validation.constraints.Email;
import lombok.*;

@Data
public class SignUpRequest {
    @Email
    private String email;
    private String password;
    private String confirmPassword;

}
