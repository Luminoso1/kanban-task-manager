package com.microservice.auth.application.dto.http;

public record ErrorResponse(int status, String message) {
}
