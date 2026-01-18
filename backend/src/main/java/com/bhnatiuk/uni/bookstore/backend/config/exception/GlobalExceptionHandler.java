package com.bhnatiuk.uni.bookstore.backend.config.exception;

import com.bhnatiuk.uni.bookstore.backend.model.dto.AppErrorResponse;
import com.bhnatiuk.uni.bookstore.backend.model.exception.api.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.HtmlUtils;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<AppErrorResponse> handleApiException(
            ApiException e,
            HttpServletRequest request) {

        HttpStatus status = mapRequest(e);

        AppErrorResponse response = new AppErrorResponse(
                status.value(),
                HtmlUtils.htmlEscape(e.getMessage()),
                HtmlUtils.htmlEscape(request.getRequestURI())
        );

        return ResponseEntity.status(status).body(response);
    }

    private HttpStatus mapRequest(ApiException e) {
        return switch (e) {
            case BadRequestException badRequestException -> HttpStatus.BAD_REQUEST;
            case ConflictException conflictException -> HttpStatus.CONFLICT;
            case NotFoundException notFoundException -> HttpStatus.NOT_FOUND;
            case UnauthorizedException unauthorizedException -> HttpStatus.UNAUTHORIZED;
            case null, default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
