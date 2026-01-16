package com.bhnatiuk.uni.bookstore.backend.util.exception;

public class LoginFailedException extends RuntimeException {
    public LoginFailedException(String message) {
        super(message);
    }
}
