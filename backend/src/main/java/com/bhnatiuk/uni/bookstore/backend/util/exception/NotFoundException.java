package com.bhnatiuk.uni.bookstore.backend.util.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
