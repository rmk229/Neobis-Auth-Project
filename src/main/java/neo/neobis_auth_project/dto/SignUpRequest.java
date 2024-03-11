package neo.neobis_auth_project.dto;

import jakarta.validation.constraints.Email;
import lombok.*;

@Data
@NoArgsConstructor
public class SignUpRequest {
    @Email
    private String email;
    private String password;
    private String confirmPassword;

}
