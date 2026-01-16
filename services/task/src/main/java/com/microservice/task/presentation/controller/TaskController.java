package com.microservice.task.presentation.controller;

import com.microservice.task.application.port.in.CreateTaskUseCase;
import com.microservice.task.application.port.in.DeleteTaskUseCase;
import com.microservice.task.application.port.in.GetTaskUseCase;
import com.microservice.task.application.port.in.UpdateTaskUseCase;
import com.microservice.task.domain.models.Task;
import com.microservice.task.presentation.controller.dto.CreateTaskRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final CreateTaskUseCase createTaskUseCase;
    private final GetTaskUseCase getTaskUseCase;
    private final UpdateTaskUseCase updateTaskUseCase;
    private final DeleteTaskUseCase deleteTaskUseCase;

    public TaskController(CreateTaskUseCase createTaskUseCase,
                          GetTaskUseCase getTaskUseCase,
                          UpdateTaskUseCase updateTaskUseCase,
                          DeleteTaskUseCase deleteTaskUseCase) {
        this.createTaskUseCase = createTaskUseCase;
        this.getTaskUseCase = getTaskUseCase;
        this.updateTaskUseCase = updateTaskUseCase;
        this.deleteTaskUseCase = deleteTaskUseCase;
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody CreateTaskRequest taskRequest) {
        final Task task = new Task(
                null,
                taskRequest.boardId(),
                taskRequest.title(),
                taskRequest.description(),
                taskRequest.status(),
                taskRequest.dueDate(),
                taskRequest.priority()
        );
        final Task createdTask = createTaskUseCase.createTask(task);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @GetMapping
    public ResponseEntity<List<Task>> getTask(){
        List<Task> tasks = this.getTaskUseCase.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/board/{boardId}")
    public ResponseEntity<List<Task>> getAllTasksByBoardId(@PathVariable("boardId")  Long id){
        List<Task> tasks = this.getTaskUseCase.getAllTasksByBoardId(id);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id){
        Task tasks = this.getTaskUseCase.getTaskById(id);
        return ResponseEntity.ok(tasks);
    }

    @PatchMapping
    public ResponseEntity<Task> updateTask(@RequestBody Task taskRequest) {
        System.out.println("Hi there Im hereeeeeeeeeeeeeeeeeee");
        Task task = this.updateTaskUseCase.updateTask(taskRequest);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id){
        this.deleteTaskUseCase.deleteTask(id);
        return ResponseEntity.ok("Task successfully removed");
    }
}
