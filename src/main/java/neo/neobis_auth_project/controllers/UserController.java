package neo.neobis_auth_project.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import neo.neobis_auth_project.service.UserService;
import neo.neobis_auth_project.dto.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "User Controller")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-in")
    @Operation(summary = "Sign in to your account")
    public SignInResponse signIn(@RequestBody SignInRequest signInRequest) {
        return userService.signIn(signInRequest);
    }

    @PostMapping("/sign-up")
    @Operation(summary = "Register", description = "Account registration")
    public SimpleResponse signUp(@RequestBody SignUpRequest signUpRequest) {
        return userService.signUp(signUpRequest);
    }

    @PostMapping("/request-password-reset")
    @Operation(summary = "Password reset request", description = "Sending a request for password reset via email")
    public void requestPasswordReset(@RequestParam("email") String email) {
        userService.requestPasswordReset(email);
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Password Reset", description = "Password reset using a generated access token")
    public void resetPassword(@RequestParam("token") String token,
                              @RequestBody ResetPasswordRequest request) {
        userService.resetPassword(token, request);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping("/allUsers")
    public List<UserResponse> getAllUserEmail(){
        return userService.getGetAllUserEmail();
    }

    @GetMapping("/confirm-email")
    public SimpleResponse confirm(@RequestParam("token") String token){
        return userService.confirmEmail(token);
    }
}