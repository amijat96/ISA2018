package com.example.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PriceListNotUpdatedException extends RuntimeException {

    public PriceListNotUpdatedException(String message) {
        super(message);
    }

    public PriceListNotUpdatedException(String message, Throwable cause) {
        super(message, cause);
    }
}