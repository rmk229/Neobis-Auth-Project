package neo.neobis_auth_project.service;

import neo.neobis_auth_project.dto.*;

public interface UserService {
    SignUpResponse signUp(SignUpRequest authenticationSignUpRequest);
    SignInResponse signIn(SignInRequest signInRequest);
    void requestPasswordReset(String email);
    void resetPassword(ResetPasswordRequest request);
}