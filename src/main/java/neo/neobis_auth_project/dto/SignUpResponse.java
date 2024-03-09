package neo.neobis_auth_project.dto;

import lombok.Getter;
import lombok.Setter;
import neo.neobis_auth_project.enums.Role;

@Getter
@Setter
public class SignUpResponse {
    private Long userId;
    private String accessToken;
    private String refreshToken;
    private String email;
    private Role role;

    public SignUpResponse(Long userId, String accessToken, String refreshToken, String email, Role role) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.email = email;
        this.role = role;
    }
}