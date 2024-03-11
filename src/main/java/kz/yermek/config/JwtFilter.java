package kz.yermek.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.yermek.entity.User;
import kz.yermek.exceptions.NotFoundException;
import kz.yermek.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private static final String BEARER_TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String tokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (tokenHeader != null && tokenHeader.startsWith(BEARER_TOKEN_PREFIX)) {
            String token = tokenHeader.substring(BEARER_TOKEN_PREFIX.length());

            if (StringUtils.hasText(token)) {

                try {
                    String email = jwtService.validateToken(token);
                    User user = userRepository.getUserByEmail(email)
                            .orElseThrow(() -> new NotFoundException(
                                    "User with email " + email + " doesn't exists!!!"
                            ));
                    SecurityContextHolder.getContext()
                            .setAuthentication(
                                    new UsernamePasswordAuthenticationToken(
                                            user.getEmail(),
                                            null,
                                            user.getAuthorities()
                                    )
                            );
                } catch (JWTVerificationException e) {
                    response.sendError
                                    (HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token!");
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}