package com.microservice.user.presentation.exception;

public record ErrorResponse(int status, String message) {
}
