package com.microservice.task.application.service;

import com.microservice.task.application.dto.BoardResponseDto;
import com.microservice.task.application.exception.BoardNotFoundException;
import com.microservice.task.application.exception.TaskNotFoundException;
import com.microservice.task.application.port.in.CreateTaskUseCase;
import com.microservice.task.application.port.in.DeleteTaskUseCase;
import com.microservice.task.application.port.in.GetTaskUseCase;
import com.microservice.task.application.port.in.UpdateTaskUseCase;
import com.microservice.task.application.port.out.TaskRepository;
import com.microservice.task.application.port.out.client.BoardServiceClient;
import com.microservice.task.domain.models.Task;
import com.microservice.task.domain.models.TaskPriority;
import com.microservice.task.domain.models.TaskStatus;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService implements CreateTaskUseCase, GetTaskUseCase, UpdateTaskUseCase, DeleteTaskUseCase {

    @Value("${gateway.internal-secret}")
    private String internalSecret;

    private final TaskRepository taskRepository;
    private final BoardServiceClient boardServiceClient;

    public TaskService(TaskRepository taskRepository, BoardServiceClient boardServiceClient) {
        this.taskRepository = taskRepository;
        this.boardServiceClient = boardServiceClient;
    }

    @Override
    public Task createTask(Task task) {
        validateBoardExists(task.boardId());
        Task newTask = new Task(
                null,
                task.boardId(),
                task.title(),
                task.description(),
                task.status() != null ? task.status() : TaskStatus.TODO,
                task.dueDate(),
                task.priority() != null ? task.priority() : TaskPriority.LOW
        );
        return this.taskRepository.save(newTask);
    }

    @Override
    public Task getTaskById(Long id) {
        return this.taskRepository
                .findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with ID " + id + " not found"));
    }

    @Override
    public List<Task> getAllTasks() {
        return this.taskRepository.findAll();
    }

    @Override
    public List<Task> getAllTasksByBoardId(Long id) {
        return this.taskRepository.findAllByBoardId(id);
    }

    @Override
    public Task updateTask(Task task) {
        Task taskUpdated = this.taskRepository.updateOne(task);
        if(taskUpdated == null){
            throw new TaskNotFoundException("Task with ID " + task.id() + " not found for update.");
        }
        return taskUpdated;
    }

    @Override
    public void deleteTask(Long id) {
        if (this.taskRepository.findById(id).isEmpty()) {
            throw new TaskNotFoundException("Task with ID " + id + " not found.");
        }

        this.taskRepository.deleteById(id);
    }

    private void validateBoardExists(Long boardId){
        try {
            BoardResponseDto board = this.boardServiceClient.getBoardById(boardId);
            if(board == null || board.id() == null){
                throw new BoardNotFoundException("Board with ID " + boardId + " not found.");
            }
        }catch (FeignException.NotFound e){
            throw new BoardNotFoundException("Board with ID " + boardId + " not found.");
        }catch (FeignException e){
            throw new RuntimeException("Error communicating with Board Service: " + e.getMessage(), e);
        }
    }
}
