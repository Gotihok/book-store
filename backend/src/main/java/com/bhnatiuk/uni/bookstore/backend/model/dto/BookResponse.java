package com.bhnatiuk.uni.bookstore.backend.model.dto;

import com.bhnatiuk.uni.bookstore.backend.model.entity.Book;

public record BookResponse(
        Long id,
        String title,
        String author,
        String publisher,
        Long ISBN
) {
    public static BookResponse from(Book bookEntity) {
        return new BookResponse(
                bookEntity.getId(),
                bookEntity.getTitle(),
                bookEntity.getAuthor(),
                bookEntity.getPublisher(),
                bookEntity.getISBN()
        );
    }
}
