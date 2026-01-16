package com.microservice.user.application.dto;

public record UserProfileDto(
        Long id,
        String providerId,
        String email,
        Boolean verified
) {
}
