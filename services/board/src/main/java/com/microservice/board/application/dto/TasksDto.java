package com.microservice.board.application.dto;


import com.microservice.board.application.dto.http.TaskResponse;

import java.util.List;

public record TasksDto(
        List<TaskResponse> todo,
        List<TaskResponse> doing,
        List<TaskResponse> done
) {

}


