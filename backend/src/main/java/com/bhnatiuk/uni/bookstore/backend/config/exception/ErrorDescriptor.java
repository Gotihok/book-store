package com.bhnatiuk.uni.bookstore.backend.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;

import java.util.function.Function;
import java.util.stream.Collectors;

//TODO: test strategies behaviour
public record ErrorDescriptor(
        HttpStatus status,
        Function<Throwable, String> messageProvider
) {

    public ErrorDescriptor(HttpStatus status) {
        this(status, defaultMessageProvider());
    }

    public ErrorDescriptor(HttpStatus status, String message) {
        this(status, e -> message);
    }

    public String message(Throwable e) {
        return messageProvider.apply(e);
    }

    //TODO: extract to strategy config
    //TODO: make different formats allowed as output message (
    //      eg. {field1: error1; field2: error2}
    // )
    private static Function<Throwable, String> defaultMessageProvider() {
        return ex -> {
            if (ex instanceof BindException bindException) {
                return bindException.getBindingResult()
                        .getFieldErrors().stream()
                        .map(fe -> fe.getField() + " " + fe.getDefaultMessage())
                        .collect(Collectors.joining(", "));
            }
            return ex.getMessage();
        };
    }
}
