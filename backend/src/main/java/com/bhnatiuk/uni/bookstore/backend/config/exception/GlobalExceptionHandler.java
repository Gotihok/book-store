package com.bhnatiuk.uni.bookstore.backend.config.exception;

import com.bhnatiuk.uni.bookstore.backend.model.dto.AppErrorResponse;
import com.bhnatiuk.uni.bookstore.backend.model.exception.CredentialsAlreadyInUseException;
import com.bhnatiuk.uni.bookstore.backend.model.exception.MalformedEmailException;
import com.bhnatiuk.uni.bookstore.backend.model.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.HtmlUtils;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Map<Class<? extends RuntimeException>, HttpStatus> EXCEPTION_MAPPING =
            Map.of(
                    MalformedEmailException.class, HttpStatus.BAD_REQUEST,
                    CredentialsAlreadyInUseException.class, HttpStatus.CONFLICT,
                    NotFoundException.class, HttpStatus.NOT_FOUND,
                    AuthenticationException.class, HttpStatus.UNAUTHORIZED
            );

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<AppErrorResponse> handleApiException(
            RuntimeException e,
            HttpServletRequest request) {

        HttpStatus status = mapRequest(e);

        AppErrorResponse response = new AppErrorResponse(
                status.value(),
                HtmlUtils.htmlEscape(e.getMessage()),
                HtmlUtils.htmlEscape(request.getRequestURI())
        );

        return ResponseEntity.status(status).body(response);
    }

    private HttpStatus mapRequest(RuntimeException e) {
        return EXCEPTION_MAPPING.getOrDefault(e.getClass(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
