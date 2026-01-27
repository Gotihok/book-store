package com.bhnatiuk.uni.bookstore.backend.service;

import com.bhnatiuk.uni.bookstore.backend.model.dto.BookCreationRequest;
import com.bhnatiuk.uni.bookstore.backend.model.dto.BookResponse;
import com.bhnatiuk.uni.bookstore.backend.model.entity.Book;

import java.util.List;

public interface BookService {
    Book create(BookCreationRequest creationRequest);

    BookResponse getById(Long id);

    List<BookResponse> find(String author, String title);
}
