package com.microservice.board.application.port.in;

import com.microservice.board.domain.model.Board;

public interface DeleteBoardUseCase {

    void deleteBoard(Long userId, Long boardId);
}
