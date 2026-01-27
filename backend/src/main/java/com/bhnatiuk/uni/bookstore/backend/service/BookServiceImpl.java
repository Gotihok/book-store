package com.bhnatiuk.uni.bookstore.backend.service;

import com.bhnatiuk.uni.bookstore.backend.model.domain.Isbn;
import com.bhnatiuk.uni.bookstore.backend.model.dto.BookCreationRequest;
import com.bhnatiuk.uni.bookstore.backend.model.dto.BookResponse;
import com.bhnatiuk.uni.bookstore.backend.model.entity.Book;
import com.bhnatiuk.uni.bookstore.backend.model.exception.InvalidIsbnException;
import com.bhnatiuk.uni.bookstore.backend.model.exception.NotFoundException;
import com.bhnatiuk.uni.bookstore.backend.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Transactional
    @Override
    public Book create(BookCreationRequest creationRequest) {
        //TODO: validate ISBN
        //TODO: convert ISBN-10 into ISBN-13

        Book book = new Book();
        book.setTitle(creationRequest.title());
        book.setAuthor(creationRequest.author());
        book.setPublisher(creationRequest.publisher());
        book.setIsbn(new Isbn(creationRequest.ISBN()));

        return bookRepository.save(book);
    }

    @Override
    public BookResponse getByIsbn(Isbn isbn) {
        return BookResponse.from(
                bookRepository.findByIsbn(isbn)
                        .orElseThrow(() -> new NotFoundException("Book not found"))
        );
    }

    @Override
    public List<BookResponse> find(String author, String title) {
        if (author != null && title != null) {
            return bookRepository
                    .findByAuthorAndTitle(author, title)
                    .stream()
                    .map(BookResponse::from)
                    .toList();
        }

        if (author != null) {
            return bookRepository
                    .findByAuthor(author)
                    .stream()
                    .map(BookResponse::from)
                    .toList();
        }

        if (title != null) {
            return bookRepository
                    .findByTitle(title)
                    .stream()
                    .map(BookResponse::from)
                    .toList();
        }

        return bookRepository
                .findAll()
                .stream()
                .map(BookResponse::from)
                .toList();
    }
}
