package com.example.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClinicNotWorkException extends RuntimeException {

    public ClinicNotWorkException(String message) {
        super(message);
    }

    public ClinicNotWorkException(String message, Throwable cause) {
        super(message, cause);
    }
}