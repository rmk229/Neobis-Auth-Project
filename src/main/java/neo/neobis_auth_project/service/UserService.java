package neo.neobis_auth_project.service;

import neo.neobis_auth_project.dto.AuthenticationSignInResponse;
import neo.neobis_auth_project.dto.AuthenticationSignUpResponse;
import neo.neobis_auth_project.dto.SignInRequest;
import neo.neobis_auth_project.dto.SignUpRequest;

public interface UserService {
    AuthenticationSignUpResponse signUp(SignUpRequest authenticationSignUpRequest);
    AuthenticationSignInResponse signIn(SignInRequest signInRequest);
    void requestPasswordReset(String email);
    void resetPassword(String email, String resetToken, String newPassword);
}
