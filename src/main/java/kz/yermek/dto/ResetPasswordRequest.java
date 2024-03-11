package kz.yermek.dto;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest(
        @NotBlank String newPassword,
        @NotBlank String confirmPassword
){
}
