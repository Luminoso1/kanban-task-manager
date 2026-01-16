package com.microservice.user.application.dto;

public record UserResponse(
        Long id,
        String providerId,
        String email,
        String password,
        Boolean verified
) {
}
