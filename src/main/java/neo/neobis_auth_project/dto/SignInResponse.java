package neo.neobis_auth_project.dto;

import neo.neobis_auth_project.enums.Role;
import lombok.Builder;

@Builder
public record SignInResponse(
    Long id,
    String accessToken,
    String refreshToken,
    String email,
    Role role
){
}