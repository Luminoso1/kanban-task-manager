package com.microservice.board.application.service;

import com.microservice.board.application.dto.BoardResponse;
import com.microservice.board.application.dto.FullBoardDto;
import com.microservice.board.application.dto.TasksDto;
import com.microservice.board.application.dto.http.TaskResponse;
import com.microservice.board.application.exception.BoardNotFoundException;
import com.microservice.board.application.port.in.CreateBoardUseCase;
import com.microservice.board.application.port.in.DeleteBoardUseCase;
import com.microservice.board.application.port.in.GetBoardUseCase;
import com.microservice.board.application.port.in.UpdateBoardUseCase;
import com.microservice.board.application.port.out.BoardRepository;
import com.microservice.board.application.port.out.client.TaskServiceClient;
import com.microservice.board.domain.model.Board;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;

@Service
public class BoardService implements CreateBoardUseCase, GetBoardUseCase, UpdateBoardUseCase, DeleteBoardUseCase {

    @Value("${gateway.internal-secret}")
    private String internalSecret;

    private final BoardRepository boardRepository;
    private final TaskServiceClient taskServiceClient;

    public BoardService(BoardRepository boardRepository, TaskServiceClient taskServiceClient) {
        this.boardRepository = boardRepository;
        this.taskServiceClient = taskServiceClient;
    }

    @Override
    public Board createBoard(Board board) {
        return this.boardRepository.save(board);
    }

    @Override
    public BoardResponse getBoardById(Long id) {
        Board board = this.boardRepository.findById(id)
                .orElseThrow(() -> new BoardNotFoundException("Board with ID " + id + " not found."));
        List<TaskResponse> tasks;
        try {
             tasks = this.taskServiceClient.getAllTasksByBoardId(id, internalSecret);
        }catch (RuntimeException e){
            throw new RuntimeException("Error obteniendo las tareas del board: " + id);
        }

        return new BoardResponse(
                board.id(),
                board.userId(),
                board.name(),
                tasks
        );
    }

    @Override
    public FullBoardDto getBoardByUserIdAndBoardId(Long userId, Long boardId) {
        Board board = this.boardRepository.findByUserIdAndBoardId(userId, boardId)
                .orElseThrow(() -> new BoardNotFoundException("Board with ID " + 1 + " not found."));

        List<TaskResponse> tasks;
        try {
            tasks = this.taskServiceClient.getAllTasksByBoardId(boardId, internalSecret);
        }catch (RuntimeException e){
            throw new RuntimeException("Error obteniendo las tareas del board: " + boardId);
        }

        System.out.println(tasks);

        var todo = tasks.stream()
                .filter(task -> "TODO".equalsIgnoreCase(task.status()))
                .toList();

        var doing = tasks.stream()
                .filter(task -> "DOING".equalsIgnoreCase(task.status()))
                .toList();

        var done = tasks.stream()
                .filter(task -> "DONE".equalsIgnoreCase(task.status()))
                .toList();

        TasksDto taskList = new TasksDto(todo, doing, done);

        return new FullBoardDto(
                board.id(),
                board.userId(),
                board.name(),
                taskList
        );
    }

    @Override
    public List<Board> getAllBoardsByUserId(Long userId) {
        return this.boardRepository.findByUserId(userId);
    }

    @Override
    public void deleteBoard(Long userId, Long boardId) {
        if(this.boardRepository.findByUserIdAndBoardId(userId, boardId).isEmpty()){
            throw new BoardNotFoundException("Board with ID " + boardId + " not found.");
        }
        this.boardRepository.deleteOne(userId, boardId);
    }

    @Override
    public void updateBoard(Board board) {
        this.boardRepository.updateOne(board);
    }
}
