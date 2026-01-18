package com.bhnatiuk.uni.bookstore.backend.model.exception.service;

public class CredentialsAlreadyInUseException extends RuntimeException {
    public CredentialsAlreadyInUseException(String message) {
        super(message);
    }

    public CredentialsAlreadyInUseException(String message, Throwable cause) {
        super(message, cause);
    }
}
