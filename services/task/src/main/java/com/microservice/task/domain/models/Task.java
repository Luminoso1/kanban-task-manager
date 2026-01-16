package com.microservice.task.domain.models;

import java.time.LocalDate;

public record Task(
        Long id,

        Long boardId,

        String title,

        String description,

        TaskStatus status,

        LocalDate dueDate,

        TaskPriority priority
) {
}


