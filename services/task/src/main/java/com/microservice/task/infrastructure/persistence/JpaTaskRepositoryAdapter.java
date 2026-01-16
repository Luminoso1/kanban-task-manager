package com.microservice.task.infrastructure.persistence;

import com.microservice.task.application.port.out.TaskRepository;
import com.microservice.task.domain.models.Task;
import com.microservice.task.infrastructure.mapper.TaskMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class JpaTaskRepositoryAdapter implements TaskRepository {

    private final TaskPersistenceRepository taskPersistenceRepository;

    public JpaTaskRepositoryAdapter(TaskPersistenceRepository taskPersistenceRepository) {
        this.taskPersistenceRepository = taskPersistenceRepository;
    }

    @Override
    public Task save(Task task) {
        TaskEntity taskEntity = TaskMapper.toPersistence(task);
        final TaskEntity savedTask = this.taskPersistenceRepository.save(taskEntity);
        return TaskMapper.toDomain(taskEntity);
    }

    @Override
    public Optional<Task> findById(Long id) {
        Optional<TaskEntity> taskEntityOptional  = this.taskPersistenceRepository.findById(id);
        return taskEntityOptional.map(TaskMapper::toDomain);
    }

    @Override
    public List<Task> findAll() {
        return this.taskPersistenceRepository.findAll()
                .stream()
                .map(TaskMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> findAllByBoardId(Long id) {
        return this.taskPersistenceRepository.findAllByBoardId(id)
                .stream()
                .map(TaskMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        this.taskPersistenceRepository.deleteById(id);
    }

    @Override
    public Task updateOne(Task task) {
        Optional<TaskEntity> taskEntityOptional = this.taskPersistenceRepository.findById(task.id());
        if(taskEntityOptional.isEmpty()) return null;

        TaskEntity taskEntity = taskEntityOptional.get();
        taskEntity.setTitle(task.title());
        taskEntity.setDescription(task.description());
        taskEntity.setStatus(task.status());
        taskEntity.setDueDate(task.dueDate());
        taskEntity.setPriority(task.priority());

        TaskEntity updatedTaskEntity = this.taskPersistenceRepository.save(taskEntity);

        return TaskMapper.toDomain(updatedTaskEntity);
    }
}
