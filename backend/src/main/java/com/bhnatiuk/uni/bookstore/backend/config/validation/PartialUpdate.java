package com.bhnatiuk.uni.bookstore.backend.config.validation;

import java.util.stream.Stream;

public interface PartialUpdate {
    boolean hasAtLeastOneNonBlankField();

    default boolean hasAtLeastOneNonBlankField(String... fields) {
        return Stream.of(fields)
                .anyMatch(field -> field != null && !field.isBlank());
    }
}
