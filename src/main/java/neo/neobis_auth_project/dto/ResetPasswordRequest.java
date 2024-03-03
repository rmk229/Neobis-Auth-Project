package neo.neobis_auth_project.dto;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest(
        @NotBlank String email,
        @NotBlank String resetToken,
        @NotBlank String newPassword
){
}
