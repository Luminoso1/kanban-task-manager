package com.microservice.board.application.dto.http;

import java.time.LocalDate;

public record TaskResponse(
        Long id,
        String title,
        String description,
        String status,
        LocalDate dueDate,
        String priority
) {
}
