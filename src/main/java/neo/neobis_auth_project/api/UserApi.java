package neo.neobis_auth_project.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import neo.neobis_auth_project.dto.*;
import neo.neobis_auth_project.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public SimpleResponse signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        return userService.signUp(signUpRequest);
    }

    @PostMapping("/requestPasswordReset")
    @Operation(summary = "Password reset request", description = "Sending a request for password reset via email")
    public void requestPasswordReset(@RequestParam("email") String email) {
        userService.requestPasswordReset(email);
    }

    @PostMapping("/resetPassword")
    @Operation(summary = "Password Reset", description = "Password reset using a generated accessToken")
    public void resetPassword(@RequestParam("token") String token,
                              @RequestBody ResetPasswordRequest request) {
        userService.resetPassword(token, request);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping("/getAllUser")
    public List<UserResponse> getAllUserEmail(){
        return userService.getGetAllUserEmail();
    }

    @GetMapping("/confirm-email")
    public SimpleResponse confirm(@RequestParam("token") String token){
        return userService.confirmEmail(token);
    }
}