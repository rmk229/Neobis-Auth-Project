package neo.neobis_auth_project.service.impl;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import neo.neobis_auth_project.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import neo.neobis_auth_project.repository.TokenRepository;
import neo.neobis_auth_project.repository.UserRepository;
import neo.neobis_auth_project.config.JwtService;
import neo.neobis_auth_project.models.Token;
import neo.neobis_auth_project.models.User;
import neo.neobis_auth_project.enums.TokenType;
import neo.neobis_auth_project.exceptions.NotFoundException;
import neo.neobis_auth_project.service.RefreshTokenService;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private static final String BEARER_TOKEN_PREFIX = "Bearer ";

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getUserId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String tokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (tokenHeader != null && tokenHeader.startsWith(BEARER_TOKEN_PREFIX)) {
            String token = tokenHeader.substring(BEARER_TOKEN_PREFIX.length());

            if (StringUtils.hasText(token)) {

                try {
                    String email = jwtService.validateToken(token);
                    User user = userRepository.getUserByEmail(email)
                            .orElseThrow(() -> new NotFoundException(
                                    "User with email " + email + " not exists!!!"
                            ));

                    if (jwtService.isValidToken(token)) {

                        String newAccessToken = jwtService.generateToken(user);
                        revokeAllUserTokens(user);
                        saveUserToken(user, newAccessToken);

                        var authResponse = TokenResponse.builder()
                                .accessToken(newAccessToken)
                                .build();

                        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
                    } else {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token!");
                    }
                } catch (JWTVerificationException e) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token!");
                }
            }
        }
    }

}