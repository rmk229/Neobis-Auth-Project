package kz.yermek.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface RefreshTokenService {
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}