package com.bhnatiuk.uni.bookstore.backend.model.exception;

public class MalformedEmailException extends RuntimeException {
    public MalformedEmailException(String message) {
        super(message);
    }

    public MalformedEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
