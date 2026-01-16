package com.microservice.board.application.dto;

import java.time.LocalDate;
import java.util.List;

public record FullBoardDto(
        Long id,
        Long userId,
        String name,
        TasksDto tasks
) {
}

