package neo.neobis_auth_project.dto;

import jakarta.validation.constraints.Email;
import lombok.Builder;


@Builder
public record SignInRequest(
        @Email
        String email,
        String password
) {
}