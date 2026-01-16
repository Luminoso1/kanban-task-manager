package com.microservice.task.application.port.in;

import com.microservice.task.domain.models.Task;

public interface UpdateTaskUseCase {
    Task updateTask(Task task);
}
