package com.bhnatiuk.uni.bookstore.backend.util.exception.controller;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
