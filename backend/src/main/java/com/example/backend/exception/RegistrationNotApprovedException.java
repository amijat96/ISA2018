package com.example.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RegistrationNotApprovedException extends RuntimeException {

    public RegistrationNotApprovedException(String message) {
        super(message);
    }

    public RegistrationNotApprovedException(String message, Throwable cause) {
        super(message, cause);
    }
}