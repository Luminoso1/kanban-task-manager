package com.microservice.user.application.port.in;

import com.microservice.user.domain.User;

public interface GetUserUseCase {

    User getUserByEmail(String email);

    User getUserById(Long id);
}
