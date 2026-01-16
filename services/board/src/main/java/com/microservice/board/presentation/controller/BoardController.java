package com.microservice.board.presentation.controller;

import com.microservice.board.application.dto.BoardResponse;
import com.microservice.board.application.dto.FullBoardDto;
import com.microservice.board.application.port.in.CreateBoardUseCase;
import com.microservice.board.application.port.in.DeleteBoardUseCase;
import com.microservice.board.application.port.in.GetBoardUseCase;
import com.microservice.board.application.port.in.UpdateBoardUseCase;
import com.microservice.board.domain.model.Board;
import com.microservice.board.presentation.controller.dto.BoardRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/boards")
public class BoardController {

    private final CreateBoardUseCase createBoardUseCase;
    private final GetBoardUseCase getBoardUseCase;
    private final UpdateBoardUseCase updateBoardUseCase;
    private final DeleteBoardUseCase deleteBoardUseCase;

    public BoardController(CreateBoardUseCase createBoardUseCase, GetBoardUseCase getBoardUseCase, UpdateBoardUseCase updateBoardUseCase, DeleteBoardUseCase deleteBoardUseCase) {
        this.createBoardUseCase = createBoardUseCase;
        this.getBoardUseCase = getBoardUseCase;
        this.updateBoardUseCase = updateBoardUseCase;
        this.deleteBoardUseCase = deleteBoardUseCase;
    }

    @PostMapping
    public ResponseEntity<Board> createBoard(@Valid @RequestBody BoardRequest boardRequest,
                                             @RequestHeader("X-Auth-UserId") Long userId){
        Board board = new Board(null, userId, boardRequest.name());
        Board savedBoard = this.createBoardUseCase.createBoard(board);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBoard);

    }

    @GetMapping
    public ResponseEntity<List<Board>> getAllBoardByUserId(@RequestHeader("X-Auth-UserId") Long userId){
        var boards = this.getBoardUseCase.getAllBoardsByUserId(userId);
        return ResponseEntity.ok(boards);
    }

    @GetMapping("/exists/{boardId}")
    public ResponseEntity<BoardResponse> getBoardById(@PathVariable("boardId") Long boardId){
        var board = this.getBoardUseCase.getBoardById(boardId);
        return ResponseEntity.ok(board);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<FullBoardDto> getBoardById(@RequestHeader("X-Auth-UserId") Long userId,
                                                     @PathVariable("boardId") Long boardId){
        var board = this.getBoardUseCase.getBoardByUserIdAndBoardId(userId, boardId);
        return ResponseEntity.ok(board);
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<String> updateBoard(@RequestHeader("X-Auth-UserId") Long userId,
                                              @Valid @RequestBody Board board){
        this.updateBoardUseCase.updateBoard(board);
        return ResponseEntity.ok("Board Successfully deleted");
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<String> deleteBoard(@RequestHeader("X-Auth-UserId") Long userId,
                                                     @PathVariable("boardId") Long boardId){
        this.deleteBoardUseCase.deleteBoard(userId, boardId);
        return ResponseEntity.ok("Board Successfully deleted");
    }
}
