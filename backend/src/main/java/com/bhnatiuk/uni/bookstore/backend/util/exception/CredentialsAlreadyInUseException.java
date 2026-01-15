package com.bhnatiuk.uni.bookstore.backend.util.exception;

public class CredentialsAlreadyInUseException extends RuntimeException {
    public CredentialsAlreadyInUseException(String message) {
        super(message);
    }
}
