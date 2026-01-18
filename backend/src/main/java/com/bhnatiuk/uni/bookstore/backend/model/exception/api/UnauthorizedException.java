package com.bhnatiuk.uni.bookstore.backend.model.exception.api;

public class UnauthorizedException extends ApiException {
    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {super(message,cause);}
}
