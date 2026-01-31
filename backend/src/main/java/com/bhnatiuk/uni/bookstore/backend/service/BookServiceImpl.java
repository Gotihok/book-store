package com.bhnatiuk.uni.bookstore.backend.service;

import com.bhnatiuk.uni.bookstore.backend.model.domain.Isbn;
import com.bhnatiuk.uni.bookstore.backend.model.dto.BookCreationRequest;
import com.bhnatiuk.uni.bookstore.backend.model.dto.BookResponse;
import com.bhnatiuk.uni.bookstore.backend.model.dto.BookUpdateRequest;
import com.bhnatiuk.uni.bookstore.backend.model.entity.Book;
import com.bhnatiuk.uni.bookstore.backend.model.exception.NotFoundException;
import com.bhnatiuk.uni.bookstore.backend.model.exception.ResourceAlreadyExistsException;
import com.bhnatiuk.uni.bookstore.backend.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Transactional
    @Override
    public Book create(BookCreationRequest creationRequest) {
        if (bookRepository.existsByIsbn(new Isbn(creationRequest.isbn())))
            throw new ResourceAlreadyExistsException("Current ISBN already exists");

        return bookRepository.save(
                BookCreationRequest.toEntity(creationRequest)
        );
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
                    .findByAuthorContainingIgnoreCaseAndTitleContainingIgnoreCase(author, title)
                    .stream()
                    .map(BookResponse::from)
                    .toList();
        }

        if (author != null) {
            return bookRepository
                    .findByAuthorContainingIgnoreCase(author)
                    .stream()
                    .map(BookResponse::from)
                    .toList();
        }

        if (title != null) {
            return bookRepository
                    .findByTitleContainingIgnoreCase(title)
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

    @Override
    public BookResponse updateByIsbn(Isbn isbn, BookUpdateRequest request) {
        Book retrievedEntity = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new NotFoundException("Book not found"));

        setIfNotBlank(request.author(), retrievedEntity::setAuthor);
        setIfNotBlank(request.title(), retrievedEntity::setTitle);
        setIfNotBlank(request.publisher(), retrievedEntity::setPublisher);

        Book savedEntity = bookRepository.save(retrievedEntity);
        return BookResponse.from(savedEntity);
    }

    private void setIfNotBlank(String value, Consumer<String> setter) {
        if (value != null && !value.isBlank()) {
            setter.accept(value);
        }
    }

    @Override
    public BookResponse deleteByIsbn(Isbn isbn) {
        Book entity = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new NotFoundException("Book not found"));

        bookRepository.delete(entity);

        return BookResponse.from(entity);
    }
}
