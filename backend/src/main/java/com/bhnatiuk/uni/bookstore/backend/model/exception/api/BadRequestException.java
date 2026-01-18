package com.bhnatiuk.uni.bookstore.backend.model.exception.api;

public class BadRequestException extends ApiException {
    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message,cause);
    }
}
