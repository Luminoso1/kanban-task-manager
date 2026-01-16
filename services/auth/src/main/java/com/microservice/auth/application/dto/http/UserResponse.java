package com.microservice.auth.application.dto.http;

public record UserResponse(
        Long id,
        String providerId,
        String email,
        String password,
        Boolean verified
) {
}
