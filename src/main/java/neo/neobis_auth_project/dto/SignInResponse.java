package neo.neobis_auth_project.dto;

import lombok.Builder;
import neo.neobis_auth_project.enums.Role;

@Builder
public record SignInResponse(
    Long id,
    String token,
    String email,
    Role role
){
}