package com.microservice.board.domain.model;

public record Board(
    Long id,
    Long userId,
    String name
) {}
