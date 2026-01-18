package com.bhnatiuk.uni.bookstore.backend.util.exception;

public abstract class BookstoreAbstractException extends RuntimeException {
    public BookstoreAbstractException(String message) {
        super(message);
    }

    public BookstoreAbstractException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Message to write in stack trace by abstract consumer
     * @return string with that message
     */
    public abstract String getCustomMessage();
}
