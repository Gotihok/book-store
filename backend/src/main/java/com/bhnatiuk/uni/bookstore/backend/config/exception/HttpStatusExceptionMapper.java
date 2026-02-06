package com.bhnatiuk.uni.bookstore.backend.config.exception;

import com.bhnatiuk.uni.bookstore.backend.model.exception.*;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Map;

import static java.util.Map.entry;

@Slf4j
@Component
public class HttpStatusExceptionMapper implements ExceptionMapper<HttpStatus> {
    private static final Map<Class<? extends Exception>, HttpStatus> EXCEPTION_MAPPING =
            Map.ofEntries(
                    entry(MalformedEmailException.class, HttpStatus.BAD_REQUEST),
                    entry(MethodArgumentNotValidException.class, HttpStatus.BAD_REQUEST),
                    entry(ConstraintViolationException.class, HttpStatus.BAD_REQUEST),
                    entry(HttpMessageNotReadableException.class, HttpStatus.BAD_REQUEST),

                    entry(CredentialsAlreadyInUseException.class, HttpStatus.CONFLICT),
                    entry(ResourceAlreadyExistsException.class, HttpStatus.CONFLICT),

                    entry(NotFoundException.class, HttpStatus.NOT_FOUND),
                    entry(NoResourceFoundException.class, HttpStatus.NOT_FOUND),

                    entry(AuthenticationException.class, HttpStatus.UNAUTHORIZED),
                    entry(BadCredentialsException.class, HttpStatus.UNAUTHORIZED),

                    entry(InvalidIsbnException.class, HttpStatus.UNPROCESSABLE_CONTENT)
            );

    @Override
    public HttpStatus map(Throwable e) {
        log.warn("Mapping to request exception: {}", e.getMessage(), e);
        return EXCEPTION_MAPPING.getOrDefault(e.getClass(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
