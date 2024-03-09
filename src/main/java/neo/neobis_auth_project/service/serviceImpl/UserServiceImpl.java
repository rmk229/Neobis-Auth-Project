package neo.neobis_auth_project.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neo.neobis_auth_project.config.JwtService;
import neo.neobis_auth_project.config.senderConfig.EmailSenderConfig;
import neo.neobis_auth_project.dto.*;
import neo.neobis_auth_project.entity.Token;
import neo.neobis_auth_project.entity.User;
import neo.neobis_auth_project.enums.Role;
import neo.neobis_auth_project.exceptions.*;
import neo.neobis_auth_project.repository.TokenRepository;
import neo.neobis_auth_project.repository.UserRepository;
import neo.neobis_auth_project.repository.jdbcTemplate.TokenJDBCTemplate;
import neo.neobis_auth_project.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.List;

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
    private final RefreshTokenServiceImpl refreshTokenServiceImpl;
    private final TokenRepository tokenRepository;
    private final TokenJDBCTemplate tokenJDBCTemplate;
    private final static long LINK_EXPIRATION_TIME_MS = 5 * 60 * 1000;

    @Override
    public SimpleResponse signUp(SignUpRequest request) {
        if (userRepository.existsUserByEmail(request.getEmail())) {
            throw new AlreadyExistException("The user with the email address " + request.getEmail() + " already exists.");
        }

        User user = new User();
        user.setEmail(request.getEmail());

        if (request.getPassword().equals(request.getVerifyPassword())) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(Role.USER);
            user.setEmailConfirmed(false);
            userRepository.save(user);
            String jwtToken = jwtService.generateTokenWithExpiration(user,LINK_EXPIRATION_TIME_MS);

            Context context = new Context();
            sendRegistryEmail(user.getEmail(),context, jwtToken);

            refreshTokenServiceImpl.saveUserToken(user,jwtToken);
            log.info("User successfully saved with the identifier:" + user.getEmail());
            return  SimpleResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("Success! Please, check your email for the confirmation"+ user.getEmail() +"the link access only for 5 min")
                    .build();
        } else {
            throw new BadCredentialException("The password and password confirmation do not match.");
        }
    }
    @Override
    public SignInResponse signIn(SignInRequest signInRequest) {
        User user = userRepository.getUserByEmail(signInRequest.email())
                .orElseThrow(() -> {
                    log.info("User with email:" + signInRequest.email() + " not found!");
                    return new NotFoundException("The user with the email address " + signInRequest.email() + " was not found!");
                });

        if (!user.isEmailConfirmed()) {
            throw new BadCredentialException("You must first confirm your email!");
        }

        if (!passwordEncoder.matches(signInRequest.password(), user.getPassword())) {
            log.info("Invalid password");
            throw new BadCredentialException("Invalid password");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRequest.email(),
                        signInRequest.password()));
        String accessToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        refreshTokenServiceImpl.saveUserToken(user, accessToken);
        return SignInResponse.builder()
                .id(user.getUserId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }



    @Override
    public void requestPasswordReset(String email) {
        User user = userRepository.getUserByEmail(email)
                .orElseThrow(() -> new NotFoundException("The user with the email address: " + email + " was not found."));

        String resetToken = jwtService.generateTokenForRequestEmail(user);
        Context context = new Context();
        refreshTokenServiceImpl.saveUserToken(user,resetToken);

        sendPasswordResetEmail(user.getEmail(), resetToken,context);
    }

    @Override
    public void resetPassword(String token, ResetPasswordRequest request) {
       User user = tokenJDBCTemplate.findUserByToken(token)
               .orElseThrow(() -> new NotFoundException("The user with the token address: " + token + " was not found."));

       if (request.newPassword().equals(request.verifyPassword())){
            user.setPassword(passwordEncoder.encode(request.newPassword()));

            userRepository.save(user);
        } else {
            throw new BadCredentialForbiddenException("Invalid or expired password reset accessToken");
        }
    }

    @Override
    public SimpleResponse confirmEmail(String token) {
        Token confirmationToken = tokenRepository.findByToken(token).orElseThrow(()->new NotFoundException("Token not found"));
        if (confirmationToken.getConfirmedAt() != null) {
            throw new AlreadyExistException("Email already confirmed");
        }

        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationToken.getUser().setEmailConfirmed(true);

        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Email successfully confirmed. Go back to your login page")
                .build();
    }

    @Override
    public List<UserResponse> getGetAllUserEmail() {
        return userRepository.getAllUser();
    }

    private void sendRegistryEmail(String email, Context context,String jwtToken) {
        context.setVariable("userEmail", email);
        context.setVariable("registry", "https://neobis-front-auth-mu.vercel.app/"+ jwtToken);
        context.setVariable("resetToken", LINK_EXPIRATION_TIME_MS);
        sendConfirmationEmail(email, "Authorization", context);
    }

    private void sendPasswordResetEmail(String email, String resetToken, Context context) {
        context.setVariable("userEmail", email);
        context.setVariable("resetLink", "https://neobis-auth-project.up.railway.app/swagger-ui/index.html#/User%20Api/signIn?token="+resetToken);
        context.setVariable("resetToken", LINK_EXPIRATION_TIME_MS);
        userResetPassword(email, resetToken, context);
    }

    private void sendConfirmationEmail(String email, String subject, Context context) {
        emailSenderConfig.sendEmailWithHTMLTemplate(email,"nurmukhamedalymbaiuulu064@gmail.com", subject, "userRegistry", context);
    }

    private void userResetPassword(String email, String subject, Context context) {
        emailSenderConfig.sendEmailWithHTMLTemplate(email,"nurmukhamedalymbaiuulu064@gmail.com", subject, "userResetPassword", context);
    }
}