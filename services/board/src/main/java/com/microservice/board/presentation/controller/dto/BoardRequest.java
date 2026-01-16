package com.microservice.board.presentation.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BoardRequest(
        @NotBlank(message = "Board name is mandatory and cannot be empty")
        @Size(min = 3, max = 50, message = "Board name must be between 3 and 50 characters")
        String name
) {
}
