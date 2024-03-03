package neo.neobis_auth_project.dto;

import lombok.Getter;
import lombok.Setter;
import neo.neobis_auth_project.enums.Role;

@Getter
@Setter
public class SignUpResponse {
    private Long userId;
    private String token;
    private String email;
    private Role role;

    public SignUpResponse(Long userId, String token, String email, Role role) {
        this.userId = userId;
        this.token = token;
        this.email = email;
        this.role = role;
    }
}
