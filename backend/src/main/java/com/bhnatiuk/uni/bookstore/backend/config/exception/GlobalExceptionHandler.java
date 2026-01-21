package com.bhnatiuk.uni.bookstore.backend.config.exception;

import com.bhnatiuk.uni.bookstore.backend.model.dto.AppErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.HtmlUtils;

//TODO: change to different routes for business and unexpected exceptions if needed
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final ExceptionMapper<HttpStatus> exceptionMapper;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AppErrorResponse> handleGlobalException(
            Exception e,
            HttpServletRequest request) {

        HttpStatus status = exceptionMapper.map(e);

        AppErrorResponse response = new AppErrorResponse(
                status.value(),
                HtmlUtils.htmlEscape(e.getMessage()),
                HtmlUtils.htmlEscape(request.getRequestURI())
        );

        return ResponseEntity.status(status).body(response);
    }
}
