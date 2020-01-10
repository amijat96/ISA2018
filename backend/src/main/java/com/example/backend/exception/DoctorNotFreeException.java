package com.example.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DoctorNotFreeException extends RuntimeException {

    public DoctorNotFreeException(String message) {
        super(message);
    }

    public DoctorNotFreeException(String message, Throwable cause) {
        super(message, cause);
    }
}
