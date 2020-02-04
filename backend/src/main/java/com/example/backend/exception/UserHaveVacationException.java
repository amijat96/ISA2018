package com.example.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserHaveVacationException extends RuntimeException {

    public UserHaveVacationException(String message) {
        super(message);
    }

    public UserHaveVacationException(String message, Throwable cause) {
        super(message, cause);
    }
}