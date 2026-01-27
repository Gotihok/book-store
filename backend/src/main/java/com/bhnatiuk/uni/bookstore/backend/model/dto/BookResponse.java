package com.bhnatiuk.uni.bookstore.backend.model.dto;

import com.bhnatiuk.uni.bookstore.backend.model.entity.Book;

public record BookResponse(
        String title,
        String author,
        String publisher,
        String ISBN
) {
    public static BookResponse from(Book bookEntity) {
        return new BookResponse(
                bookEntity.getTitle(),
                bookEntity.getAuthor(),
                bookEntity.getPublisher(),
                bookEntity.getIsbn().getValue()
        );
    }
}
