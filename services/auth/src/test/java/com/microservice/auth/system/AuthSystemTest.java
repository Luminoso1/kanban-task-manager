package com.microservice.auth.system;

import com.microservice.auth.application.dto.CreateUserDto;
import com.microservice.auth.application.dto.http.UserResponse;
import com.microservice.auth.application.exception.UserCreationException;
import com.microservice.auth.application.port.out.PasswordEncoderRepository;
import com.microservice.auth.infrastructure.adapter.out.UserFeignClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AuthSystemTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate rest;

    @MockitoBean
    private UserFeignClient userFeignClient;

    @MockitoBean
    private PasswordEncoderRepository passwordEncoderRepository;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    private UserResponse mockUser() {
        return new UserResponse(
                1L,
                "provider-id",
                "test@example.com",
                "encoded-password",
                true
        );
    }

    @Test
    void shouldRegisterAndLoginSuccessfully(){
        when(userFeignClient.createUser(any(), anyString()))
                .thenReturn(mockUser());

        when(passwordEncoderRepository.encode(anyString()))
                .thenReturn("encoded-password");

        when(userFeignClient.getUserByEmail(anyString(), anyString()))
                .thenReturn(mockUser());

        when(passwordEncoderRepository.matches(anyString(), anyString()))
                .thenReturn(true);

        // Sign up
        CreateUserDto signup = new CreateUserDto("test@example.com", "Password123");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateUserDto> signupEntity = new HttpEntity<>(signup, headers);

        ResponseEntity<String> signupResponse =
                rest.postForEntity(url("/api/v1/auth/signup"), signupEntity, String.class);

        assertEquals(HttpStatus.CREATED, signupResponse.getStatusCode());
        assertEquals("Registration successful", signupResponse.getBody());

        List<String> signupCookies = signupResponse.getHeaders().get(HttpHeaders.SET_COOKIE);

        assertNotNull(signupCookies);
        assertFalse(signupCookies.isEmpty());

        // Sign in
        CreateUserDto login = new CreateUserDto("test@example.com", "Password123");
        HttpEntity<CreateUserDto> loginEntity = new HttpEntity<>(login, headers);

        ResponseEntity<String> loginResponse =
                rest.postForEntity(url("/api/v1/auth/signin"), loginEntity, String.class);

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertEquals("Login successful", loginResponse.getBody());

        List<String> loginCookies = loginResponse.getHeaders().get(HttpHeaders.SET_COOKIE);
        assertNotNull(loginCookies);
        assertFalse(loginCookies.isEmpty());

        boolean accessTokenExists = loginCookies.stream()
                .anyMatch(c -> c.contains("accessToken"));

        boolean refreshTokenExists = loginCookies.stream()
                .anyMatch(c -> c.contains("refreshToken"));

        assertTrue(accessTokenExists);
        assertTrue(refreshTokenExists);
    }

    @Test
    void shouldFailRegisterWhenEmailAlreadyExists() {
        when(userFeignClient.createUser(any(), anyString()))
                .thenThrow(new UserCreationException("User with this email already exists"));

        CreateUserDto signup = new CreateUserDto("existing@example.com", "Password123");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateUserDto> signupEntity = new HttpEntity<>(signup, headers);

        ResponseEntity<String> response =
                rest.postForEntity(url("/api/v1/auth/signup"), signupEntity, String.class);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("User with this email already exists"));
    }

    @Test
    void shouldFailLoginWhenPasswordIncorrect() {
        when(userFeignClient.getUserByEmail(anyString(), anyString()))
                .thenReturn(mockUser());

        when(passwordEncoderRepository.matches(anyString(), anyString()))
                .thenReturn(false);

        CreateUserDto login = new CreateUserDto("test@example.com", "WrongPass");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateUserDto> entity = new HttpEntity<>(login, headers);

        ResponseEntity<String> response =
                rest.postForEntity(url("/api/v1/auth/signin"), entity, String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Invalid credentials"));
    }
}
