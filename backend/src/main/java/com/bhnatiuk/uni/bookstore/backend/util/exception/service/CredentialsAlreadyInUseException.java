package com.bhnatiuk.uni.bookstore.backend.util.exception.service;

public class CredentialsAlreadyInUseException extends RuntimeException {
    public CredentialsAlreadyInUseException(String message) {
        super(message);
    }
}
