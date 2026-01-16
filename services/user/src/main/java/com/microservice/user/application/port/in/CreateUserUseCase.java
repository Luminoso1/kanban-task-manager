package com.microservice.user.application.port.in;

import com.microservice.user.application.dto.CreateUserDto;
import com.microservice.user.domain.User;

public interface CreateUserUseCase {

    User createNewUser(CreateUserDto data);
}
