package com.bhnatiuk.uni.bookstore.backend.repository;

import com.bhnatiuk.uni.bookstore.backend.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByAuthorAndTitle(String author, String title);

    List<Book> findByAuthor(String author);

    List<Book> findByTitle(String title);
}
