package com.microservice.auth.application.dto;

public record AuthTokens(String accessToken, String refreshToken) {}