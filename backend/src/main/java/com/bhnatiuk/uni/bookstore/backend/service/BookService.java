package com.bhnatiuk.uni.bookstore.backend.service;

import com.bhnatiuk.uni.bookstore.backend.model.domain.Isbn;
import com.bhnatiuk.uni.bookstore.backend.model.dto.BookCreationRequest;
import com.bhnatiuk.uni.bookstore.backend.model.dto.BookResponse;
import com.bhnatiuk.uni.bookstore.backend.model.dto.BookUpdateRequest;
import com.bhnatiuk.uni.bookstore.backend.model.entity.Book;

import java.util.List;

public interface BookService {
    Book create(BookCreationRequest creationRequest);

    List<BookResponse> find(String author, String title);

    BookResponse getByIsbn(Isbn isbn);

    BookResponse updateByIsbn(Isbn isbn, BookUpdateRequest request);

    BookResponse deleteByIsbn(Isbn isbn);
}
