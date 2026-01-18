package com.bhnatiuk.uni.bookstore.backend.util.exception.controller;

public class LoginFailedException extends BookstoreAbstractControllerException {
    public LoginFailedException(String message) {
        super(message);
    }

    @Override
    public String getCustomMessage() {
        return "Login failed";
    }

    @Override
    public String getControllerMessage() {
        return "";
    }
}
