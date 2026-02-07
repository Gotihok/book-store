package com.bhnatiuk.uni.bookstore.backend.config.exception;

import com.bhnatiuk.uni.bookstore.backend.model.exception.*;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Map;

import static java.util.Map.entry;

@Deprecated
@Slf4j
public class HttpStatusExceptionMapper implements ExceptionMapper<HttpStatus> {
    private static final Map<Class<? extends Exception>, HttpStatus> STAUS_EXCEPTION_MAP =
            Map.ofEntries(
                    entry(MalformedEmailException.class, HttpStatus.BAD_REQUEST), // CUSTOM
                    entry(MethodArgumentNotValidException.class, HttpStatus.BAD_REQUEST), // SPRING
                    entry(ConstraintViolationException.class, HttpStatus.BAD_REQUEST), // JAKARTA
                    entry(HttpMessageNotReadableException.class, HttpStatus.BAD_REQUEST), // SPRING

                    entry(CredentialsAlreadyInUseException.class, HttpStatus.CONFLICT), // CUSTOM
                    entry(ResourceAlreadyExistsException.class, HttpStatus.CONFLICT), // CUSTOM

                    entry(NotFoundException.class, HttpStatus.NOT_FOUND), // CUSTOM
                    entry(NoResourceFoundException.class, HttpStatus.NOT_FOUND), // SPRING

                    entry(AuthenticationException.class, HttpStatus.UNAUTHORIZED), // SPRING
                    entry(BadCredentialsException.class, HttpStatus.UNAUTHORIZED), // SPRING

                    entry(InvalidIsbnException.class, HttpStatus.UNPROCESSABLE_CONTENT) // CUSTOM
            );

    @Override
    public HttpStatus map(Throwable e) {
        HttpStatus status = STAUS_EXCEPTION_MAP.getOrDefault(e.getClass(), HttpStatus.INTERNAL_SERVER_ERROR);
        log.warn("Mapping exception: {} to status code: {}", e.getClass().getSimpleName(), status);
        return status;
    }
}
