package com.bhnatiuk.uni.bookstore.backend.model.exception.api;

public class NotFoundException extends ApiException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
