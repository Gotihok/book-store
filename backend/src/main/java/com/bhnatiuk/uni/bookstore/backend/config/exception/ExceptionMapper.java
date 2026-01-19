package com.bhnatiuk.uni.bookstore.backend.config.exception;

public interface ExceptionMapper<T> {
    T map(Throwable e);
}
