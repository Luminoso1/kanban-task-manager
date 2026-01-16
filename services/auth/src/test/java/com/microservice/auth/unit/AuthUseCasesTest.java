package com.microservice.auth.unit;

import com.microservice.auth.application.dto.AuthTokens;
import com.microservice.auth.application.dto.CreateUserDto;
import com.microservice.auth.application.dto.http.UserResponse;
import com.microservice.auth.application.exception.InvalidCredentialsException;
import com.microservice.auth.application.exception.UserCreationException;
import com.microservice.auth.application.port.out.PasswordEncoderRepository;
import com.microservice.auth.application.port.out.TokenRepository;
import com.microservice.auth.application.port.out.UserClientRepository;
import com.microservice.auth.application.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthUseCasesTest {

    @Mock
    private UserClientRepository userClientRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private PasswordEncoderRepository passwordEncoderRepository;

    @InjectMocks
    private AuthService authService;

    @Nested
    class RegistrationTests {

        @Test
        void shouldRegisterUserSuccessfully() {
            // Given
            CreateUserDto userDto = new CreateUserDto("email@example.dev", "P4ssword123%");
            UserResponse mockUser = new UserResponse(
                    1L,
                    "providerId",
                    "email@example.dev",
                    "encodedPassword",
                    false
            );

            when(passwordEncoderRepository.encode(userDto.password())).thenReturn("encodedPassword");
            when(userClientRepository.createUser(any())).thenReturn(mockUser);
            when(tokenRepository.generateAccessToken(eq(mockUser.email()), anyMap())).thenReturn("accessToken");
            when(tokenRepository.generateRefreshToken(mockUser.email())).thenReturn("refreshToken");

            // When
            AuthTokens tokens = authService.registerUser(userDto);

            // Then
            assertNotNull(tokens);

            assertEquals("accessToken", "");

            assertEquals("refreshToken", tokens.refreshToken());
        }

        @Test
        void shouldThrowUserCreationExceptionWhenUserServiceFails() {
            // Arrange
            CreateUserDto userDto = new CreateUserDto("email@example.dev", "password123");

            when(passwordEncoderRepository.encode(any())).thenReturn("encodedPassword");
            when(userClientRepository.createUser(any()))
                    .thenThrow(new UserCreationException("User creation failed by external service"));

            // Act & Assert
            UserCreationException exception = assertThrows(
                    UserCreationException.class,
                    () -> authService.registerUser(userDto)
            );

            assertEquals("User creation failed by external service", exception.getMessage());
        }

        @Test
        void shouldThrowUserCreationExceptionWhenUserServiceReturnsNull() {
            // Arrange
            CreateUserDto userDto = new CreateUserDto("email@example.dev", "password123");

            when(passwordEncoderRepository.encode(any())).thenReturn("encodedPassword");
            when(userClientRepository.createUser(any())).thenReturn(null);

            // Act & Assert
            UserCreationException exception = assertThrows(
                    UserCreationException.class,
                    () -> authService.registerUser(userDto)
            );

            assertEquals("User service returned null after user creation attempt.", exception.getMessage());
        }

        @Test
        void shouldWrapUnexpectedRuntimeExceptionFromUserService() {
            // Arrange
            CreateUserDto userDto = new CreateUserDto("email@example.dev", "password123");

            when(passwordEncoderRepository.encode(any())).thenReturn("encodedPassword");
            when(userClientRepository.createUser(any()))
                    .thenThrow(new RuntimeException("Connection timeout"));

            // Act & Assert (Note: The service should preferably rethrow a more specific custom exception)
            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> authService.registerUser(userDto)
            );

            // The exception message should contain both the wrapper and the root cause for better traceability
            assertTrue(exception.getMessage().contains("Failed to register user due to an unexpected issue with user service"));
            assertTrue(exception.getMessage().contains("Connection timeout"));
        }
    }

    @Nested
    class LoginTests {

        @Test
        void shouldThrowInvalidCredentialsWhenUserNotFound() {
            // Arrange
            CreateUserDto userDto = new CreateUserDto(
                    "missing@example.dev",
                    "somePassword"
            );

            when(userClientRepository.getUserByEmail(userDto.email()))
                    .thenReturn(Optional.empty());

            // Act & Assert
            InvalidCredentialsException exception = assertThrows(
                    InvalidCredentialsException.class,
                    () -> authService.loginUser(userDto)
            );

            assertEquals("User not found.", exception.getMessage());
        }


        @Test
        void shouldThrowInvalidCredentialsExceptionOnWrongPassword(){
            // Arrange
            CreateUserDto userDto = new CreateUserDto("email@example.dev", "wrongPassword");
            // Use 'false' for 'enabled' to test that password check is still the main failure point
            UserResponse mockUser = new UserResponse(1L, "providerId", "email@example.dev", "encodedPassword", false);

            when(userClientRepository.getUserByEmail(userDto.email())).thenReturn(Optional.of(mockUser));
            when(passwordEncoderRepository.matches(userDto.password(), mockUser.password())).thenReturn(false);

            // Act & Assert
            InvalidCredentialsException exception = assertThrows(
                    InvalidCredentialsException.class,
                    () -> authService.loginUser(userDto)
            );

            assertEquals("Invalid credentials: Incorrect password.", exception.getMessage());
        }

        @Test
        void shouldLoginSuccessfully() {
            // Arrange
            CreateUserDto userDto = new CreateUserDto("email@example.dev", "P4ssword123%");
            UserResponse mockUser = new UserResponse(
                    1L,
                    "providerId",
                    "email@example.dev",
                    "encodedPassword",
                    true // User is enabled
            );

            when(userClientRepository.getUserByEmail(userDto.email())).thenReturn(Optional.of(mockUser));
            when(passwordEncoderRepository.matches(userDto.password(), mockUser.password())).thenReturn(true);
            when(tokenRepository.generateAccessToken(eq(mockUser.email()), anyMap())).thenReturn("loginAccessToken");
            when(tokenRepository.generateRefreshToken(mockUser.email())).thenReturn("loginRefreshToken");

            // Act
            AuthTokens tokens = authService.loginUser(userDto);

            // Assert
            assertNotNull(tokens);
            assertEquals("loginAccessToken", tokens.accessToken());
            assertEquals("loginRefreshToken", tokens.refreshToken());
        }
    }
}