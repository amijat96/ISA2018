package com.example.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RoomTypeNotFoundException extends RuntimeException {

    public RoomTypeNotFoundException(String message) {
        super(message);
    }

    public RoomTypeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
