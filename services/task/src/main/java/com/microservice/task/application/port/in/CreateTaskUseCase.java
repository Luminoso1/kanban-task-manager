package com.microservice.task.application.port.in;

import com.microservice.task.domain.models.Task;

public interface CreateTaskUseCase {
    Task createTask(Task task);
}
