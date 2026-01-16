package com.microservice.task.presentation.controller.dto;

import com.microservice.task.domain.models.TaskPriority;
import com.microservice.task.domain.models.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateTaskRequest(
        @NotNull(message = "Board ID is mandatory")
        Long boardId,

        @NotBlank(message = "Title is mandatory and cannot be empty")
        @Size(min = 3, max = 50, message = "Title must be between 3 and 50 characters")
        String title,

        String description,

        TaskStatus status,

        LocalDate dueDate,

        TaskPriority priority
) {
}
