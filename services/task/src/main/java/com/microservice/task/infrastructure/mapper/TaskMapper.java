package com.microservice.task.infrastructure.mapper;

import com.microservice.task.domain.models.Task;
import com.microservice.task.infrastructure.persistence.TaskEntity;

public class TaskMapper {

    public  static Task toDomain(TaskEntity entity){
        if (entity == null) {
            return null;
        }
        return new Task(
                entity.getId(),
                entity.getBoardId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getStatus(),
                entity.getDueDate(),
                entity.getPriority()
        );
    }

    public static TaskEntity toPersistence(Task task){
        if (task == null) {
            return null;
        }
        return new TaskEntity(
                task.id(),
                task.boardId(),
                task.title(),
                task.description(),
                task.status(),
                task.dueDate(),
                task.priority()
        );
    }
}
