package neo.neobis_auth_project.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import neo.neobis_auth_project.dto.*;
import neo.neobis_auth_project.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "User Api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserApi {

    private final UserService userService;

    @PostMapping("/signIn")
    @Operation(summary = "Sign in to your account")
    public SignInResponse signIn(@RequestBody @Valid SignInRequest signInRequest) {
        return userService.signIn(signInRequest);
    }

    @PostMapping("/signUp")
    @Operation(summary = "Register", description = "Account registration")
    public SignUpResponse signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        return userService.signUp(signUpRequest);
    }

    @PostMapping("/requestPasswordReset")
    @Operation(summary = "Password reset request", description = "Sending a request for password reset via email")
    public void requestPasswordReset(@RequestParam("email") String email) {
        userService.requestPasswordReset(email);
    }

    @PostMapping("/resetPassword")
    @Operation(summary = "Password Reset", description = "Password reset using a generated token")
    public void resetPassword(
            @RequestBody ResetPasswordRequest request) {
        userService.resetPassword(request);
    }
}