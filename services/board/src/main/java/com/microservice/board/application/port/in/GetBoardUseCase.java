package com.microservice.board.application.port.in;

import com.microservice.board.application.dto.BoardResponse;
import com.microservice.board.application.dto.FullBoardDto;
import com.microservice.board.domain.model.Board;

import java.util.List;

public interface GetBoardUseCase {
    BoardResponse getBoardById(Long id);

    FullBoardDto getBoardByUserIdAndBoardId(Long userId, Long boardId);

    List<Board> getAllBoardsByUserId(Long userId);
}
