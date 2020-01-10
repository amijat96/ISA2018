package com.example.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ExaminationNotFoundException extends RuntimeException {

    public ExaminationNotFoundException(String message) {
        super(message);
    }

    public ExaminationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}