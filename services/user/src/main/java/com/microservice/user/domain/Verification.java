package com.microservice.user.domain;

import java.time.LocalDateTime;

public record Verification(
        Long id,
        Long userId,
        VerificationType type,
        LocalDateTime createdAt,
        LocalDateTime expiresAt
) {
}
