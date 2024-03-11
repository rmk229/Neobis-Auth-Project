package kz.yermek.service;

import kz.yermek.dto.*;


import java.util.List;

public interface UserService {
    SimpleResponse signUp(SignUpRequest authenticationSignUpRequest);
    SignInResponse signIn(SignInRequest signInRequest);
    void requestPasswordReset(String email);
    void resetPassword(String token, ResetPasswordRequest request);
    SimpleResponse confirmEmail(String token);
    List<UserResponse> getGetAllUserEmail();
}