package com.microservice.auth.application.exception;

public abstract class AuthException extends RuntimeException {
    public AuthException(String message){
        super(message);
    }

    public AuthException(String message, Throwable cause){
        super(message, cause);
    }
}

