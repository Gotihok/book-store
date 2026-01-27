package com.bhnatiuk.uni.bookstore.backend.controller;

import com.bhnatiuk.uni.bookstore.backend.model.domain.Isbn;
import com.bhnatiuk.uni.bookstore.backend.model.dto.BookCreationRequest;
import com.bhnatiuk.uni.bookstore.backend.model.dto.BookResponse;
import com.bhnatiuk.uni.bookstore.backend.model.entity.Book;
import com.bhnatiuk.uni.bookstore.backend.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping("/api/books/new")
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookCreationRequest creationRequest) {
        Book createdBook = bookService.create(creationRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/books/")
                .buildAndExpand(createdBook.getId())
                .toUri();

        return ResponseEntity.created(location).body(BookResponse.from(createdBook));
    }

    @GetMapping("/api/books/{isbn}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable String isbn) {
        return ResponseEntity.ok(
                bookService.getByIsbn(new Isbn(isbn))
        );
    }

    @GetMapping("/api/books")
    public ResponseEntity<List<BookResponse>> findBooks(
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String title
    ) {
        return ResponseEntity.ok(
                bookService.find(author, title)
        );
    }
}
