package com.bhnatiuk.uni.bookstore.backend.util.exception.controller;

import com.bhnatiuk.uni.bookstore.backend.util.exception.BookstoreAbstractException;

public abstract class BookstoreAbstractControllerException extends BookstoreAbstractException {
    public BookstoreAbstractControllerException(String message) {
        super(message);
    }

    public BookstoreAbstractControllerException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Message to expose for controller layer
     * @return string with that message
     */
    public abstract String getControllerMessage();
}
