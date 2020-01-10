package com.example.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DoctorNotWorkException extends RuntimeException {

    public DoctorNotWorkException(String message) {
        super(message);
    }

    public DoctorNotWorkException(String message, Throwable cause) {
        super(message, cause);
    }
}
