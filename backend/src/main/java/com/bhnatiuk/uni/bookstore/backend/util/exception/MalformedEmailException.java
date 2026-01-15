package com.bhnatiuk.uni.bookstore.backend.util.exception;

public class MalformedEmailException extends RuntimeException {
    public MalformedEmailException(String message) {
        super(message);
    }
}
