package com.bhnatiuk.uni.bookstore.backend.model.entity.converter;

import com.bhnatiuk.uni.bookstore.backend.model.domain.Isbn;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class IsbnConverter implements AttributeConverter<Isbn, String> {
    @Override
    public String convertToDatabaseColumn(Isbn isbn) {
        return isbn == null ? null : isbn.getValue();
    }

    @Override
    public Isbn convertToEntityAttribute(String inDbIsbn) {
        return inDbIsbn == null ? null : new Isbn(inDbIsbn);
    }
}
