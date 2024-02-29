package neo.neobis_auth_project.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neo.neobis_auth_project.config.JwtService;
import neo.neobis_auth_project.dto.AuthenticationSignInResponse;
import neo.neobis_auth_project.dto.AuthenticationSignUpResponse;
import neo.neobis_auth_project.dto.SignInRequest;
import neo.neobis_auth_project.dto.SignUpRequest;
import neo.neobis_auth_project.entity.User;
import neo.neobis_auth_project.enums.Role;
import neo.neobis_auth_project.exceptions.AlreadyExistException;
import neo.neobis_auth_project.exceptions.BadCredentialException;
import neo.neobis_auth_project.exceptions.NotFoundException;
import neo.neobis_auth_project.repository.UserRepository;
import neo.neobis_auth_project.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    @Override
    public AuthenticationSignUpResponse signUp(SignUpRequest request) {
        if (userRepository.existsUserByEmail(request.getEmail())) {
            throw new AlreadyExistException("Пользователь с адресом электронной почты:"
                    + request.getEmail() + " уже существует");
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);

        log.info("Пользователь успешно сохранен с идентификатором:" + user.getEmail());
        String token = jwtService.generateToken(user);
        return new AuthenticationSignUpResponse(
                user.getUserId(),
                token,
                user.getEmail(),
                user.getRole()
        );
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
            String token = jwtService.generateToken(user);
            return AuthenticationSignInResponse.builder()
                    .id(user.getUserId())
                    .token(token)
                    .email(user.getEmail())
                    .role(user.getRole())
                    .build();
        }
    }