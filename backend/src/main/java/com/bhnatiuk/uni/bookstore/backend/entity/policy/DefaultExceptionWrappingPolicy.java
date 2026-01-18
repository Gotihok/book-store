package com.bhnatiuk.uni.bookstore.backend.entity.policy;

import com.bhnatiuk.uni.bookstore.backend.util.exception.BookstoreAbstractException;
import com.bhnatiuk.uni.bookstore.backend.util.exception.controller.ExceptionTranslationFailedException;
import org.springframework.stereotype.Component;

//TODO: make generic instantiation
@Component
public class DefaultExceptionWrappingPolicy implements ExceptionWrappingPolicy {
    @Override
    public Throwable wrap(
            Throwable source,
            Class<? extends BookstoreAbstractException> targetType
    ) {

        if (source instanceof Error) {
            throw (Error) source;
        }

        try {
            return targetType
                    .getConstructor(String.class, Throwable.class)
                    .newInstance(source.getMessage(), source);
        } catch (ReflectiveOperationException e) {
            return new ExceptionTranslationFailedException(
                    "Failed to translate exception to " + targetType.getSimpleName(),
                    source
            );
        }
    }
}
