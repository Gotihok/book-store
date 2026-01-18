package com.bhnatiuk.uni.bookstore.backend.util.exception.controller;

public class ExceptionTranslationFailedException extends BookstoreAbstractControllerException {
    public ExceptionTranslationFailedException(String message) {
        super(message);
    }

    public ExceptionTranslationFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getCustomMessage() {
        return "";
    }

    @Override
    public String getControllerMessage() {
        return "";
    }
}
