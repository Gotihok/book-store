package com.bhnatiuk.uni.bookstore.backend.model.exception.api;

public class ConflictException extends ApiException {
    public ConflictException(String message) {
        super(message);
    }

    public ConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
