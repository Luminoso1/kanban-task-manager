package com.microservice.user.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserDto (
        @NotBlank(message = "email is mandatory")
        @Email
        String email,

        @NotBlank(message = "Password is mandatory")
        @Size(min = 8, message = "Password must have 8 min characters")
        String password
) {
}
