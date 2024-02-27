package neo.neobis_auth_project.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import neo.neobis_auth_project.dto.AuthenticationSignInResponse;
import neo.neobis_auth_project.dto.AuthenticationSignUpResponse;
import neo.neobis_auth_project.dto.SignInRequest;
import neo.neobis_auth_project.dto.SignUpRequest;
import neo.neobis_auth_project.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "User Api")
@CrossOrigin(origins = "*", maxAge = 3600)
@PermitAll
public class UserApi {

    private final UserService userService;

    @PostMapping("/signIn")
    @Operation(summary = "Вход в свой аккаунт")
    public AuthenticationSignInResponse signIn(@RequestBody SignInRequest signInRequest) {
        return userService.signIn(signInRequest);
    }

    @PostMapping("/signUp")
    @Operation(summary = "Зарегистрироваться", description = "Регистрация  аккаунта")
    public AuthenticationSignUpResponse signUp(@RequestBody SignUpRequest signUpRequest) {
        return userService.signUp(signUpRequest);
    }
}
