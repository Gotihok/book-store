package com.bhnatiuk.uni.bookstore.backend.model.exception;

public class InvalidIsbnException extends RuntimeException {
    public InvalidIsbnException(String message) {
        super(message);
    }

    public InvalidIsbnException(String message, Throwable cause) {}
}
