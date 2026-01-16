package com.microservice.user.presentation.controller;

import com.microservice.user.application.dto.CreateUserDto;
import com.microservice.user.application.dto.GetUserDto;
import com.microservice.user.application.dto.UserProfileDto;
import com.microservice.user.application.dto.UserResponse;
import com.microservice.user.application.port.in.CreateUserUseCase;
import com.microservice.user.application.port.in.GetUserUseCase;
import com.microservice.user.domain.User;
import com.microservice.user.infrastructure.persistence.mapper.UserMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final GetUserUseCase getUserUseCase;

    public UserController(CreateUserUseCase createUserUseCase, GetUserUseCase getUserUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.getUserUseCase = getUserUseCase;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createNewUser(@Valid @RequestBody CreateUserDto data){
        User user = this.createUserUseCase.createNewUser(data);
        UserResponse response = UserMapper.toResponse(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> getUserProfile(@RequestHeader("X-Auth-UserEmail") String email){
        System.out.println("EMAIL: " + email);
        User user = this.getUserUseCase.getUserByEmail(email);
        UserProfileDto response = UserMapper.toProfile(user);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable("email") String email){
        User user = this.getUserUseCase.getUserByEmail(email);
        UserResponse response = UserMapper.toResponse(user);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id){
        User user = this.getUserUseCase.getUserById(id);
        UserResponse response = UserMapper.toResponse(user);
        return ResponseEntity.ok(response);
    }

    // verify email

    // reset password

}

