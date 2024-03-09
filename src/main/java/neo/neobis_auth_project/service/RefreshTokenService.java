package neo.neobis_auth_project.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import neo.neobis_auth_project.entity.Token;

import java.io.IOException;
import java.util.Optional;

public interface RefreshTokenService {
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}