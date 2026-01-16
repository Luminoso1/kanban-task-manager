package com.microservice.auth.application.exception;

import com.microservice.auth.application.dto.http.ErrorResponse;
import lombok.Getter;

@Getter
public class UserServiceCommunicationException extends AuthException{
    private final ErrorResponse userServiceError;

    public UserServiceCommunicationException(String message, ErrorResponse userServiceError) {
        super(message);
        this.userServiceError = userServiceError;
    }

    public UserServiceCommunicationException(String message, Throwable cause) {
        super(message, cause);
        this.userServiceError = null;
    }

}
