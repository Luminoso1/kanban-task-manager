package com.microservice.user.domain;

public record User (
        Long id,
        String providerId,
        String email,
        String password,
        Boolean verified
) {
}
