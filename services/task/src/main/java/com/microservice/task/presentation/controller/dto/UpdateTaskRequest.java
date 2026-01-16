package com.microservice.task.presentation.controller.dto;

import com.microservice.task.domain.models.TaskPriority;
import com.microservice.task.domain.models.TaskStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UpdateTaskRequest(
        @NotNull
        Long id,

        @NotNull
        String title,

        String description,

        TaskStatus status,

        LocalDate dueDate,

        TaskPriority priority
) {
}
