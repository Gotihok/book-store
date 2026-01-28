package com.bhnatiuk.uni.bookstore.backend.repository;

import com.bhnatiuk.uni.bookstore.backend.model.domain.Isbn;
import com.bhnatiuk.uni.bookstore.backend.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    //TODO: add containing to each search except of isbn

    List<Book> findByAuthorContainingAndTitleContaining(String author, String title);

    List<Book> findByAuthorContaining(String author);

    List<Book> findByTitleContaining(String title);

    Optional<Book> findByIsbn(Isbn isbn);

    boolean existsByIsbn(Isbn isbn);
}
