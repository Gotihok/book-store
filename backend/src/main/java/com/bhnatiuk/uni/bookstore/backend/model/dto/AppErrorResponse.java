package com.bhnatiuk.uni.bookstore.backend.model.dto;

public record AppErrorResponse(
        int status,
        String message,
        String path
) {}
