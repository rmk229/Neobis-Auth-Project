package neo.neobis_auth_project.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neo.neobis_auth_project.config.JwtService;
import neo.neobis_auth_project.config.senderConfig.EmailSenderConfig;
import neo.neobis_auth_project.dto.*;
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
    private final static long LINK_EXPIRATION_TIME_MS = 5 * 60 * 1000;

    @Override
    public SignUpResponse signUp(SignUpRequest request) {
        if (userRepository.existsUserByEmail(request.getEmail())) {
            throw new AlreadyExistException("The user with the email address " + request.getEmail() + " already exists.");
        }

        User user = new User();
        user.setEmail(request.getEmail());

        if (request.getPassword().equals(request.getVerifyPassword())) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(Role.USER);
            userRepository.save(user);
            String token = jwtService.generateToken(user, LINK_EXPIRATION_TIME_MS);
            Context context = new Context();
            sendRegistryEmail(user.getEmail(), context);
            log.info("User successfully saved with the identifier:" + user.getEmail());
            return new SignUpResponse(
                    user.getUserId(),
                    token,
                    user.getEmail(),
                    user.getRole()
            );
        } else {
            throw new BadCredentialException("The password and password confirmation do not match.");
        }
    }
    @Override
    public SignInResponse signIn(SignInRequest signInRequest) {
        User user = userRepository.getUserByEmail(signInRequest.email()).orElseThrow(() ->{
                log.info("User with email:"+signInRequest.email()+" not found!");
            return new NotFoundException("The user with the email address " + signInRequest.email() + " was not found!");
        });

        if (!passwordEncoder.matches(signInRequest.password(), user.getPassword())) {
            log.info("Invalid password");
            throw new BadCredentialException("Invalid password");
        }
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            signInRequest.email(),
                            signInRequest.password()));
            String token = jwtService.generateToken(user, LINK_EXPIRATION_TIME_MS);
            return SignInResponse.builder()
                    .id(user.getUserId())
                    .token(token)
                    .email(user.getEmail())
                    .role(user.getRole())
                    .build();
        }


    @Override
    public void requestPasswordReset(String email) {
        User user = userRepository.getUserByEmail(email)
                .orElseThrow(() -> new NotFoundException("The user with the email address: " + email + " was not found."));

        String resetToken = jwtService.generateToken(user, LINK_EXPIRATION_TIME_MS);
        Context context = new Context();
        sendPasswordResetEmail(user.getEmail(), resetToken,context);
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        if (jwtService.isValidToken(request.resetToken())) {
            User user = userRepository.getUserByEmail(request.email())
                    .orElseThrow(() -> new NotFoundException("The user with the email address:" + request.email() + " was not found."));

            user.setPassword(passwordEncoder.encode(request.newPassword()));
            userRepository.save(user);
        } else {
            throw new BadCredentialForbiddenException("Invalid or expired password reset token");
        }
    }

    private void sendRegistryEmail(String email, Context context) {
        context.setVariable("userEmail", email);
        context.setVariable("registry", "https://neobis-front-auth-mu.vercel.app/");
        context.setVariable("resetToken", LINK_EXPIRATION_TIME_MS / (60 * 1000));
        System.out.println("Reset Link: " + context.getVariable("resetLink"));
        sendConfirmationEmail(email, "Authorization", context);
    }

    private void sendPasswordResetEmail(String email, String resetToken, Context context) {
        context.setVariable("userEmail", email);
        context.setVariable("resetLink", "https://neobis-auth-project.up.railway.app/swagger-ui/index.html#/User%20Api/signIn?token="+resetToken);
        context.setVariable("resetToken", LINK_EXPIRATION_TIME_MS / (60 * 1000));
        userResetPassword(email, resetToken, context);
    }

    private void sendConfirmationEmail(String email, String subject, Context context) {
        emailSenderConfig.sendEmailWithHTMLTemplate(email,"nurmukhamedalymbaiuulu064@gmail.com", subject, "userRegistry", context);
    }

    private void userResetPassword(String email, String subject, Context context) {
        emailSenderConfig.sendEmailWithHTMLTemplate(email,"nurmukhamedalymbaiuulu064@gmail.com", subject, "userResetPassword", context);
    }
}