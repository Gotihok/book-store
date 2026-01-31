package com.bhnatiuk.uni.bookstore.backend.model.dto;

import com.bhnatiuk.uni.bookstore.backend.config.validation.AtLeastOneField;
import com.bhnatiuk.uni.bookstore.backend.config.validation.PartialUpdate;

@AtLeastOneField
public record BookUpdateRequest(
        String title,
        String author,
        String publisher
) implements PartialUpdate {

    @Override
    public boolean hasAtLeastOneNonBlankField() {
        return PartialUpdate.super.hasAtLeastOneNonBlankField(title, author, publisher);
    }
}
