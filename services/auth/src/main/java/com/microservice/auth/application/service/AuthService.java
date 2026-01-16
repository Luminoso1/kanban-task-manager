package com.microservice.auth.application.service;

import com.microservice.auth.application.dto.AuthTokens;
import com.microservice.auth.application.dto.CreateUserDto;
import com.microservice.auth.application.dto.http.UserResponse;
import com.microservice.auth.application.exception.InvalidCredentialsException;
import com.microservice.auth.application.exception.InvalidTokenException;
import com.microservice.auth.application.exception.UserCreationException;
import com.microservice.auth.application.port.in.AuthUseCase;
import com.microservice.auth.application.port.out.PasswordEncoderRepository;
import com.microservice.auth.application.port.out.TokenRepository;
import com.microservice.auth.application.port.out.UserClientRepository;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService implements AuthUseCase {

    private final UserClientRepository userClientRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoderRepository passwordEncoderRepository;

    public AuthService(UserClientRepository userClientRepository,
                       TokenRepository tokenRepository,
                       PasswordEncoderRepository passwordEncoderRepository) {
        this.userClientRepository = userClientRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoderRepository = passwordEncoderRepository;
    }

    @Override
    public AuthTokens registerUser(CreateUserDto data) {
        String encodedPassword = this.passwordEncoderRepository.encode(data.password());
        CreateUserDto userToCreate = new CreateUserDto(data.email(), encodedPassword);

        UserResponse createdUser;
        try {
            createdUser = this.userClientRepository.createUser(userToCreate);
        } catch (UserCreationException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to register user due to an unexpected issue with user service: " + e.getMessage(), e);
        }

        if(createdUser == null){
            throw new UserCreationException("User service returned null after user creation attempt.");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", createdUser.id());
        claims.put("email", createdUser.email());
        claims.put("verified", createdUser.verified());

        String accessToken = this.tokenRepository.generateAccessToken(createdUser.email(), claims);
        String refreshToken = this.tokenRepository.generateRefreshToken(createdUser.email());

        return new AuthTokens(accessToken, refreshToken);
    }

    @Override
    public AuthTokens loginUser(CreateUserDto data) {
        UserResponse user;
        try {
            user = this.userClientRepository.getUserByEmail(data.email())
                    .orElse(null);
            if (user == null) {
                throw new InvalidCredentialsException("User not found.");
            }

        } catch (InvalidCredentialsException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to log in due to an unexpected issue with user service: " + e.getMessage(), e);
        }

        boolean passwordMatch = this.passwordEncoderRepository.matches(data.password(), user.password());

        if(!passwordMatch){
            throw new InvalidCredentialsException("Invalid credentials: Incorrect password.");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.id());
        claims.put("email", user.email());
        claims.put("verified", user.verified());

        String accessToken = this.tokenRepository.generateAccessToken(user.email(), claims);
        String refreshToken = this.tokenRepository.generateRefreshToken(user.email());

        return new AuthTokens(accessToken, refreshToken);

    }

    @Override
    public AuthTokens refreshAccessToken(String refreshToken) {
        if (!this.tokenRepository.validateToken(refreshToken)) {
            throw new InvalidTokenException("Invalid refresh token.");
        }

        String userEmail;
        try {
            userEmail = this.tokenRepository.getSubjectFromToken(refreshToken);
        } catch (ExpiredJwtException e) {
            throw new InvalidTokenException("Refresh token expired.");
        } catch (Exception e) {
            throw new InvalidTokenException("Malformed or invalid refresh token.");
        }


        UserResponse user;
        try {
            user = this.userClientRepository.getUserByEmail(userEmail)
                    .orElse(null);

            if (user == null) {
                throw new InvalidTokenException("User not found for refresh token (unexpected empty response).");
            }

        } catch (InvalidCredentialsException e) {
            throw new InvalidTokenException("User not found for refresh token: " + e.getMessage());
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to refresh token due to an unexpected issue with user service: " + e.getMessage(), e);
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.id());
        claims.put("email", user.email());
        claims.put("verified", user.verified());

        String accessToken = this.tokenRepository.generateAccessToken(user.email(), claims);
        String newRefreshToken = this.tokenRepository.generateRefreshToken(user.email());

        return new AuthTokens(accessToken, newRefreshToken);
    }
}
