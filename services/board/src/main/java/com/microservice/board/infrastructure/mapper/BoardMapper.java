package com.microservice.board.infrastructure.mapper;

import com.microservice.board.domain.model.Board;
import com.microservice.board.infrastructure.persistence.BoardEntity;

public class BoardMapper {

    public static Board toDomain(BoardEntity entity){
        if(entity == null){
            return null;
        }
        return new Board(entity.getId(), entity.getUserId(), entity.getName());
    }

    public static BoardEntity toPersistence(Board board){
        if(board == null){
            return null;
        }
        return new BoardEntity(
                board.id(),
                board.userId(),
                board.name()
        );
    }
}
