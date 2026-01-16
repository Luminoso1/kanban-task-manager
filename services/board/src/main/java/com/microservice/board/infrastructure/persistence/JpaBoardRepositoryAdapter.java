package com.microservice.board.infrastructure.persistence;

import com.microservice.board.application.port.out.BoardRepository;
import com.microservice.board.domain.model.Board;
import com.microservice.board.infrastructure.mapper.BoardMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Repository
public class JpaBoardRepositoryAdapter implements BoardRepository {

    private final BoardPersistenceRepository boardPersistenceRepository;

    public JpaBoardRepositoryAdapter(BoardPersistenceRepository boardPersistenceRepository) {
        this.boardPersistenceRepository = boardPersistenceRepository;
    }

    @Override
    public Board save(Board board) {
        BoardEntity boardEntity = BoardMapper.toPersistence(board);
        BoardEntity savedBoard = this.boardPersistenceRepository.save(boardEntity);
        return BoardMapper.toDomain(savedBoard);
    }

    @Override
    public Optional<Board> findById(Long id) {
        return this.boardPersistenceRepository.findById(id).map(BoardMapper::toDomain);
    }

    @Override
    public Optional<Board> findByUserIdAndBoardId(Long userId, Long boardId) {
        return this.boardPersistenceRepository.findByUserIdAndBoardId(userId, boardId)
                .map(BoardMapper::toDomain);
    }


    @Override
    public List<Board> findByUserId(Long userId) {
        return this.boardPersistenceRepository.findAllByUserId(userId)
                .stream()
                .map(BoardMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void updateOne(Board board) {
        Optional<BoardEntity> boardEntityOptional = this.boardPersistenceRepository
                .findByUserIdAndBoardId(board.userId(), board.id());

        if(boardEntityOptional.isEmpty()) return;

        BoardEntity boardEntity = boardEntityOptional.get();

        boardEntity.setName(board.name());

        BoardEntity updatedBoardEntity = this.boardPersistenceRepository.save(boardEntity);

    }

    @Override
    public void deleteOne(Long userId, Long boardId) {
        this.boardPersistenceRepository.deleteById(boardId);
    }

}
