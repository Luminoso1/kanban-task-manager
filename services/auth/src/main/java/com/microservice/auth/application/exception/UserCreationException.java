package com.microservice.auth.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserCreationException extends AuthException{

    public UserCreationException(String message) {
        super(message);
    }
}
