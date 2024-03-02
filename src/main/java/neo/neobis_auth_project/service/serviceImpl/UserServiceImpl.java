package neo.neobis_auth_project.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neo.neobis_auth_project.config.JwtService;
import neo.neobis_auth_project.config.senderConfig.EmailSenderConfig;
import neo.neobis_auth_project.dto.AuthenticationSignInResponse;
import neo.neobis_auth_project.dto.AuthenticationSignUpResponse;
import neo.neobis_auth_project.dto.SignInRequest;
import neo.neobis_auth_project.dto.SignUpRequest;
import neo.neobis_auth_project.entity.User;
import neo.neobis_auth_project.enums.Role;
import neo.neobis_auth_project.exceptions.AlreadyExistException;
import neo.neobis_auth_project.exceptions.BadCredentialException;
import neo.neobis_auth_project.exceptions.BadCredentialForbiddenException;
import neo.neobis_auth_project.exceptions.NotFoundException;
import neo.neobis_auth_project.repository.UserRepository;
import neo.neobis_auth_project.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmailSenderConfig emailSenderConfig;
    private final static long LINK_EXPIRATION_TIME_MS = 5 * 60 * 1000; // 5 minutes in milliseconds

    @Override
    public AuthenticationSignUpResponse signUp(SignUpRequest request) {
        if (userRepository.existsUserByEmail(request.getEmail())) {
            throw new AlreadyExistException("Пользователь с адресом электронной почты:"
                    + request.getEmail() + " уже существует");
        }

        User user = new User();
        user.setEmail(request.getEmail());

        if (request.getPassword().equals(request.getVerifyPassword())) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(Role.USER);
            userRepository.save(user);

            log.info("Пользователь успешно сохранен с идентификатором:" + user.getEmail());

            // Generate token with expiration time
            String token = jwtService.generateToken(user, LINK_EXPIRATION_TIME_MS);
            Context context = new Context();
            // Send confirmation email with token
            sendConfirmationEmail(user.getEmail(), token,context);

            return new AuthenticationSignUpResponse(
                    user.getUserId(),
                    token,
                    user.getEmail(),
                    user.getRole()
            );
        } else {
            throw new BadCredentialException("Не совпадают пароль и подтверждение пароля.");
        }
    }
    @Override
    public AuthenticationSignInResponse signIn(SignInRequest signInRequest) {
        User user = userRepository.getUserByEmail(signInRequest.email()).orElseThrow(() ->{
                log.info("User with email:"+signInRequest.email()+" not found!");
            return new NotFoundException("Пользователь с адресом электронной почты:" + signInRequest.email() + " не найден!");
        });

        if (!passwordEncoder.matches(signInRequest.password(), user.getPassword())) {
            log.info("Недействительный пароль");
            throw new BadCredentialException("Недействительный пароль");
        }
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            signInRequest.email(),
                            signInRequest.password()));
            String token = jwtService.generateToken(user, LINK_EXPIRATION_TIME_MS);
            return AuthenticationSignInResponse.builder()
                    .id(user.getUserId())
                    .token(token)
                    .email(user.getEmail())
                    .role(user.getRole())
                    .build();
        }


    @Override
    public void requestPasswordReset(String email) {
        // Check if the user exists
        User user = userRepository.getUserByEmail(email)
                .orElseThrow(() -> new NotFoundException("Пользователь с адресом электронной почты:" + email + " не найден"));

        // Generate reset token with expiration time
        String resetToken = jwtService.generateToken(user, LINK_EXPIRATION_TIME_MS);

        // Send password reset email with reset token
        sendPasswordResetEmail(user.getEmail(), resetToken);
    }

    @Override
    public void resetPassword(String email, String resetToken, String newPassword) {
        // Verify the reset token
        if (jwtService.isValidToken(resetToken)) {
            // Update the user's password in the database
            User user = userRepository.getUserByEmail(email)
                    .orElseThrow(() -> new NotFoundException("Пользователь с адресом электронной почты:" + email + " не найден"));

            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            // Optionally, you can generate a new access token for the user after password reset
            String newAccessToken = jwtService.generateToken(user, LINK_EXPIRATION_TIME_MS);

            // Send notification email about password reset
            sendPasswordResetNotificationEmail(user.getEmail());
        } else {
            // Handle invalid or expired reset token
            throw new BadCredentialForbiddenException("Неверный или просроченный токен для сброса пароля");
        }
    }

    // Updated helper method to send password reset email
    private void sendPasswordResetEmail(String email, String resetToken) {
        Context context = new Context();

        // Add necessary context variables for the email template
        context.setVariable("userEmail", email);
        context.setVariable("resetLink", "https://neobis-auth-project.up.railway.app/swagger-ui/index.html#/User%20Api/signIn" + resetToken);
        context.setVariable("expirationTime", LINK_EXPIRATION_TIME_MS / (60 * 1000)); // Convert milliseconds to minutes

        // Send email with reset token
        sendConfirmationEmail(email, "Сброс пароля", context);
    }

    // Helper method to send password reset notification email
    private void sendPasswordResetNotificationEmail(String email) {
        Context context = new Context();
        // Add necessary context variables for the email template
        context.setVariable("userEmail", email);

        // Send email about successful password reset
        sendConfirmationEmail(email, "Пароль успешно сброшен", context);
    }

    private void sendConfirmationEmail(String email, String subject, Context context) {
        emailSenderConfig.sendEmailWithHTMLTemplate(email,"nurmukhamedalymbaiuulu064@gmail.com", subject, "userRegistry", context);
    }
}