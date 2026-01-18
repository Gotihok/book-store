package com.bhnatiuk.uni.bookstore.backend.entity.policy;

import com.bhnatiuk.uni.bookstore.backend.util.exception.BookstoreAbstractException;

public interface ExceptionWrappingPolicy {
    Throwable wrap(
            Throwable source,
            Class<? extends BookstoreAbstractException> targetType
    );
}
