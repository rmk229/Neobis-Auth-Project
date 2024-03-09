package neo.neobis_auth_project.dto;

import lombok.Builder;
import neo.neobis_auth_project.enums.Role;

@Builder
public record SignInResponse(
    Long id,
    String accessToken,
    String refreshToken,
    String email,
    Role role
){
}