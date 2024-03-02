package neo.neobis_auth_project.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
public class UserApi {

    private final UserService userService;

    @PostMapping("/signIn")
    @Operation(summary = "Вход в свой аккаунт")
    public AuthenticationSignInResponse signIn(@RequestBody @Valid SignInRequest signInRequest) {
        return userService.signIn(signInRequest);
    }

    @PostMapping("/signUp")
    @Operation(summary = "Зарегистрироваться", description = "Регистрация  аккаунта")
    public AuthenticationSignUpResponse signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        return userService.signUp(signUpRequest);
    }

    @PostMapping("/requestPasswordReset")
    @Operation(summary = "Запрос на сброс пароля", description = "Отправка запроса на сброс пароля по электронной почте")
    public void requestPasswordReset(@RequestParam("email") String email) {
        userService.requestPasswordReset(email);
    }

    @PostMapping("/resetPassword")
    @Operation(summary = "Сброс пароля", description = "Сброс пароля с использованием сгенерированного токена")
    public void resetPassword(
            @RequestParam("email") String email,
            @RequestParam("resetToken") String resetToken,
            @RequestParam("newPassword") String newPassword
    ) {
        userService.resetPassword(email, resetToken, newPassword);
    }
}