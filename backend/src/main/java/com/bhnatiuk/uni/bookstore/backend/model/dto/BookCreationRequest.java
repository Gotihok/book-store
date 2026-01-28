package com.bhnatiuk.uni.bookstore.backend.model.dto;

import com.bhnatiuk.uni.bookstore.backend.model.domain.Isbn;
import com.bhnatiuk.uni.bookstore.backend.model.entity.Book;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record BookCreationRequest(

        @NotBlank
        String title,

        @NotBlank
        String author,

        @NotBlank
        String publisher,

        @NotBlank
        @Pattern(regexp = "^(\\d{9}[\\dX]|\\d{13})$")
        String ISBN
) {
        public static Book toEntity(BookCreationRequest request) {
                Book entity= new Book();
                entity.setTitle(request.title());
                entity.setAuthor(request.author());
                entity.setPublisher(request.publisher());
                entity.setIsbn(new Isbn(request.ISBN()));
                return entity;
        }
}
