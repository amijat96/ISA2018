package com.example.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TypeOfExaminationNotFoundException extends RuntimeException {

    public TypeOfExaminationNotFoundException(String message) {
        super(message);
    }

    public TypeOfExaminationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}