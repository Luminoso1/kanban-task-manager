package com.microservice.auth.presentation.controller;

import com.microservice.auth.application.dto.AuthTokens;
import com.microservice.auth.application.dto.CreateUserDto;
import com.microservice.auth.application.port.in.AuthUseCase;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Value("${application.auth.cookie-path:/}")
    private String cookiePath;

    private final AuthUseCase authUseCase;

    public AuthController(AuthUseCase authUseCase) {
        this.authUseCase = authUseCase;
    }

    @PostMapping("/signin")
    ResponseEntity<?> login(@Valid  @RequestBody CreateUserDto data, HttpServletResponse response){
        AuthTokens tokens = authUseCase.loginUser(data);
        setTokensInCookies(response, tokens.accessToken(), tokens.refreshToken());
        return ResponseEntity.ok("Login successful");
    }






    @PostMapping("/signup")
    ResponseEntity<?> register(@Valid  @RequestBody CreateUserDto data, HttpServletResponse response){
        AuthTokens tokens = this.authUseCase.registerUser(data);
        setTokensInCookies(response, tokens.accessToken(), tokens.refreshToken());
        return ResponseEntity.status(HttpStatus.CREATED).body("Registration successful");
    }

    @GetMapping("/signout")
    ResponseEntity<?> logout(HttpServletResponse response){
        deleteTokensFromCookies(response);
        return ResponseEntity.ok("Logout successful");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@CookieValue(name = "refreshToken", required = false) String refreshToken, HttpServletResponse response) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token not found");
        }
        AuthTokens newTokens = this.authUseCase.refreshAccessToken(refreshToken);
        setTokensInCookies(response, newTokens.accessToken(), newTokens.refreshToken());
        return ResponseEntity.ok("Tokens refreshed successfully");
    }


    private void setTokensInCookies(HttpServletResponse response, String accessToken, String refreshToken) {
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setPath(cookiePath);
        accessTokenCookie.setMaxAge(24 * 60 * 60);
        response.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath(cookiePath);
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(refreshTokenCookie);
    }

    private void deleteTokensFromCookies(HttpServletResponse response) {
        Cookie accessTokenCookie = new Cookie("accessToken", null);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setPath(cookiePath);
        accessTokenCookie.setMaxAge(0);
        response.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath(cookiePath);
        refreshTokenCookie.setMaxAge(0);
        response.addCookie(refreshTokenCookie);
    }
}
