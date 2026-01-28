package com.bhnatiuk.uni.bookstore.backend.config.exception;

import com.bhnatiuk.uni.bookstore.backend.model.exception.CredentialsAlreadyInUseException;
import com.bhnatiuk.uni.bookstore.backend.model.exception.MalformedEmailException;
import com.bhnatiuk.uni.bookstore.backend.model.exception.NotFoundException;
import com.bhnatiuk.uni.bookstore.backend.model.exception.ResourceAlreadyExistsException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Map;

@Slf4j
@Component
public class HttpStatusExceptionMapper implements ExceptionMapper<HttpStatus> {
    private static final Map<Class<? extends Exception>, HttpStatus> EXCEPTION_MAPPING =
            Map.of(
                    MalformedEmailException.class, HttpStatus.BAD_REQUEST,
                    MethodArgumentNotValidException.class, HttpStatus.BAD_REQUEST,
                    ConstraintViolationException.class, HttpStatus.BAD_REQUEST,

                    CredentialsAlreadyInUseException.class, HttpStatus.CONFLICT,
                    ResourceAlreadyExistsException.class, HttpStatus.CONFLICT,

                    NotFoundException.class, HttpStatus.NOT_FOUND,
                    NoResourceFoundException.class, HttpStatus.NOT_FOUND,

                    AuthenticationException.class, HttpStatus.UNAUTHORIZED
            );

    @Override
    public HttpStatus map(Throwable e) {
        log.warn("Mapping to request exception: {}", e.getMessage(), e);
        return EXCEPTION_MAPPING.getOrDefault(e.getClass(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
