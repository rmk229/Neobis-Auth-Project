package neo.neobis_auth_project.dto;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest(
        @NotBlank String newPassword,
        @NotBlank String verifyPassword
){
}
