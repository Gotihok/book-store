package com.bhnatiuk.uni.bookstore.backend.util.exception.service;

public class MalformedEmailException extends RuntimeException {
    public MalformedEmailException(String message) {
        super(message);
    }
}
