package com.microservice.auth.integration;

import com.microservice.auth.application.dto.CreateUserDto;
import com.microservice.auth.application.dto.http.UserResponse;
import com.microservice.auth.application.exception.InvalidCredentialsException;
import com.microservice.auth.application.exception.UserCreationException;
import com.microservice.auth.application.port.out.PasswordEncoderRepository;
import com.microservice.auth.infrastructure.adapter.out.UserFeignClient;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserFeignClient userFeignClient;

    @MockitoBean
    private PasswordEncoderRepository passwordEncoder;

    private final String BASE = "/api/v1/auth";

    private UserResponse mockUser(){
        return new UserResponse(1L, "google", "test@example.com", "hashed", true);
    }


    @Test
    void shouldLoginSuccessfully() throws Exception {
        when(userFeignClient.getUserByEmail(anyString(), anyString()))
                .thenReturn(mockUser());

        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        String json = """
            {
                "email": "test@example.com",
                "password": "Password123"
            }
            """;

        mockMvc.perform(post(BASE + "/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("accessToken"))
                .andExpect(cookie().exists("refreshToken"))
                .andExpect(content().string("Login successful"));
    }

    @Test
    void shouldFailRegisterWhenEmailAlreadyExists() throws Exception {
        when(userFeignClient.createUser(any(), anyString()))
                .thenThrow(new UserCreationException("User with this email already exists"));
        String json = """
            {
                "email": "existing@example.com",
                "password": "Password123"
            }
            """;
        mockMvc.perform(post(BASE + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User with this email already exists"));
    }

    @Test
    void shouldThrowInvalidCredentialsExceptionWhenEmailIncorrect() throws Exception {
        when(userFeignClient.getUserByEmail(anyString(), anyString()))
                .thenThrow(new InvalidCredentialsException("User not found"));

        String json = """
            {
                "email": "existing@example.com",
                "password": "Password123"
            }
            """;

        mockMvc.perform(post(BASE + "/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("User not found"));

    }

    @Test
    void shouldThrowInvalidCredentialsExceptionWhenPasswordIncorrect() throws Exception {

        when(userFeignClient.getUserByEmail(anyString(), anyString()))
                .thenReturn(mockUser());

        when(passwordEncoder.matches(anyString(), anyString()))
                .thenThrow(new InvalidCredentialsException("Invalid credentials: Incorrect password"));

        String json = """
            {
                "email": "existing@example.com",
                "password": "Password123"
            }
            """;

        mockMvc.perform(post(BASE + "/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid credentials: Incorrect password"));

    }


    @Test
    void shouldRegisterSuccessfully() throws Exception {
        Mockito.when(userFeignClient.createUser(any(), anyString()))
                .thenReturn(mockUser());

        String json = """
        {
            "email": "test@example.com",
            "password": "Password123"
        }
    """;

        mockMvc.perform(post(BASE + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().string("Registration successful"))
                .andExpect(cookie().exists("accessToken"))
                .andExpect(cookie().exists("refreshToken"))
                .andExpect(cookie().httpOnly("accessToken", true))
                .andExpect(cookie().httpOnly("refreshToken", true));
    }



    @Test
    void shouldFailRegisterOnUserServiceError() throws Exception {
        when(userFeignClient.createUser(any(), anyString()))
                .thenThrow(new RuntimeException("Connection refused"));

        mockMvc.perform(post(BASE + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"email":"fail@example.com","password":"Pass123"}
                            """))
                .andExpect(status().isBadRequest());
    }
}
