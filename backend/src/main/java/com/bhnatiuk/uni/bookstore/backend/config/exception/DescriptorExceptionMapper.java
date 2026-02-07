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

@Slf4j
public class DescriptorExceptionMapper implements ExceptionMapper<ErrorDescriptor> {
    private static final ErrorDescriptor FALLBACK_DESCRIPTOR = new ErrorDescriptor(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Unexpected error occurred"
    );

    //TODO: normalize message for exceptions with non default message
    //TODO: normalize dto validation messages
    private static final Map<Class<? extends Throwable>, ErrorDescriptor> DESCRIPTOR_EXCEPTION_MAP = Map.ofEntries(
            /// BAD REQUEST
            entry(
                    MalformedEmailException.class,
                    new ErrorDescriptor(HttpStatus.BAD_REQUEST)
            ),
            entry(
                    MethodArgumentNotValidException.class,
                    new ErrorDescriptor(HttpStatus.BAD_REQUEST)
            ),
            entry(
                    ConstraintViolationException.class,
                    new ErrorDescriptor(HttpStatus.BAD_REQUEST, "Constraint violated")
            ),
            entry(
                    HttpMessageNotReadableException.class,
                    new ErrorDescriptor(HttpStatus.BAD_REQUEST, "Message not readable")
            ),

            /// CONFLICT
            entry(
                    CredentialsAlreadyInUseException.class,
                    new ErrorDescriptor(HttpStatus.CONFLICT)
            ),
            entry(
                    ResourceAlreadyExistsException.class,
                    new ErrorDescriptor(HttpStatus.CONFLICT)
            ),

            /// NOT FOUND
            entry(
                    NotFoundException.class,
                    new ErrorDescriptor(HttpStatus.NOT_FOUND)
            ),
            entry(
                    NoResourceFoundException.class,
                    new ErrorDescriptor(HttpStatus.NOT_FOUND, "Resource not found")
            ),

            /// UNAUTHORIZED
            entry(
                    AuthenticationException.class,
                    new ErrorDescriptor(HttpStatus.UNAUTHORIZED, "Authentication failed")
            ),
            entry(
                    BadCredentialsException.class,
                    new ErrorDescriptor(HttpStatus.UNAUTHORIZED, "Invalid credentials")
            ),

            /// UNPROCESSABLE CONTENT
            entry(
                    InvalidIsbnException.class,
                    new ErrorDescriptor(HttpStatus.UNPROCESSABLE_CONTENT, "Unprocessable ISBN")
            )
    );

    @Override
    public ErrorDescriptor map(Throwable e) {
        ErrorDescriptor descriptor = DESCRIPTOR_EXCEPTION_MAP.getOrDefault(e.getClass(), FALLBACK_DESCRIPTOR);
        log.warn("Mapping exception: {} to descriptor for: {}", e.getClass().getSimpleName(), descriptor);
        return descriptor;
    }
}
