package com.microservice.user.application.service;

import com.microservice.user.application.dto.CreateUserDto;
import com.microservice.user.application.exception.UserAlreadyExistsException;
import com.microservice.user.application.exception.UserNotFoundException;
import com.microservice.user.application.port.in.CreateUserUseCase;
import com.microservice.user.application.port.in.GetUserUseCase;
import com.microservice.user.application.port.out.UserRepository;
import com.microservice.user.domain.User;
import org.springframework.stereotype.Service;

@Service
public class UserService implements CreateUserUseCase, GetUserUseCase {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public User createNewUser(CreateUserDto data) {

        if(this.userRepository.findByEmail(data.email()).isPresent()){
            throw new UserAlreadyExistsException(data.email());
        }

        User newUser = new User(null, null, data.email(), data.password(), false);
        return this.userRepository.save(newUser);
    }

    @Override
    public User getUserByEmail(String email) {
        return this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with Email: " + email + " not found"));
    }

    @Override
    public User getUserById(Long id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID: " + id + " not found"));
    }
}
