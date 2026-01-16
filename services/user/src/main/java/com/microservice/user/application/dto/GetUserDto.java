package com.microservice.user.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record GetUserDto(
        @NotBlank(message = "email is mandatory")
        @Email
        String email
){}