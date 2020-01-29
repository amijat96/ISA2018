package com.example.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PriceListAlreadyExistException extends RuntimeException {

    public PriceListAlreadyExistException(String message) {
        super(message);
    }

    public PriceListAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}