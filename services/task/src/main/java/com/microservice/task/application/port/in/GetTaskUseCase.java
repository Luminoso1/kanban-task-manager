package com.microservice.task.application.port.in;

import com.microservice.task.domain.models.Task;

import java.util.List;

public interface GetTaskUseCase {
    Task getTaskById(Long id);

    List<Task> getAllTasks();

    List<Task> getAllTasksByBoardId(Long id);

}
