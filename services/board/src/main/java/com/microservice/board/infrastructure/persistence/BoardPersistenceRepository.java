package com.microservice.board.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardPersistenceRepository extends JpaRepository<BoardEntity, Long> {

//    @Query("SELECT t FROM TaskEntity t WHERE t.boardId = :boardId")
//    List<TaskEntity> findAllByBoardId(@Param("boardId") Long boardId);

    @Query("SELECT b FROM BoardEntity b WHERE b.userId = :userId")
    List<BoardEntity> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT b FROM BoardEntity b WHERE b.userId = :userId AND b.id = :boardId")
    Optional<BoardEntity> findByUserIdAndBoardId(@Param("userId") Long userId,
                                           @Param("boardId") Long boardId);

//    @Modifying
//    @Query("DELETE b FROM BoardEntity b WHERE b.userId = :userId AND b.id = :boardId")
//    void deleteByUserIdAndBoardId(@Param("userId") Long userId,
//                                                 @Param("boardId") Long boardId);


}
