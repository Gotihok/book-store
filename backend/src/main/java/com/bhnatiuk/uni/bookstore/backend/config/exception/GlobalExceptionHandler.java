package com.bhnatiuk.uni.bookstore.backend.config.exception;

import com.bhnatiuk.uni.bookstore.backend.model.dto.AppErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.HtmlUtils;

//TODO: change to different routes for business and unexpected exceptions
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final ExceptionMapper<ErrorDescriptor> exceptionMapper;

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<AppErrorResponse> handleGlobalException(
            Throwable t,
            HttpServletRequest request) {

        ErrorDescriptor descriptor = exceptionMapper.map(t);

        //TODO: make a generalization with generic to reflect ExceptionMapper<T>
        // and allow easier switching between mapping implementations
        AppErrorResponse response = new AppErrorResponse(
                descriptor.status().value(),
                HtmlUtils.htmlEscape(descriptor.message(t)),
                HtmlUtils.htmlEscape(request.getRequestURI())
        );

        return ResponseEntity.status(descriptor.status().value()).body(response);
    }
}
