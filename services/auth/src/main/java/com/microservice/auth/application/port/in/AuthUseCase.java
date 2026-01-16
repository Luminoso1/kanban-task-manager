package com.microservice.auth.application.port.in;

import com.microservice.auth.application.dto.AuthTokens;
import com.microservice.auth.application.dto.CreateUserDto;

public interface AuthUseCase {
    AuthTokens registerUser(CreateUserDto data);
    AuthTokens loginUser(CreateUserDto data);
    AuthTokens refreshAccessToken(String refreshToken);
}
