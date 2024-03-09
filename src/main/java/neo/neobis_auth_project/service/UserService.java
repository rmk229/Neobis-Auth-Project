package neo.neobis_auth_project.service;

import neo.neobis_auth_project.dto.*;

import java.util.List;

public interface UserService {
    SimpleResponse signUp(SignUpRequest authenticationSignUpRequest);
    SignInResponse signIn(SignInRequest signInRequest);
    void requestPasswordReset(String email);
    void resetPassword(String token,ResetPasswordRequest request);
    SimpleResponse confirmEmail(String token);
    List<UserResponse> getGetAllUserEmail();
}