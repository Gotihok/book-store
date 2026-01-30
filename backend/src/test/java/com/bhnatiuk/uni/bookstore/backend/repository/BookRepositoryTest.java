package com.bhnatiuk.uni.bookstore.backend.repository;

import com.bhnatiuk.uni.bookstore.backend.model.domain.Isbn;
import com.bhnatiuk.uni.bookstore.backend.model.entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:18.1");

    @DynamicPropertySource
    static void datasourceProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private BookRepository bookRepository;

    private void saveBook(String author, String title, String publisher, String isbnValue) {
        Book book = new Book();
        book.setAuthor(author);
        book.setTitle(title);
        book.setPublisher(publisher);
        book.setIsbn(new Isbn(isbnValue));
        bookRepository.save(book);
    }

    @Test
    void findByAuthorContainingAndTitleContaining_shouldReturnMatchingBooks() {
        saveBook("Robert Martin", "Clean Code", "Prentice Hall", "9780306406157");
        saveBook("Robert Martin", "Testing in Action", "Prentice Hall", "9784861076039");
        saveBook("Bob Martin", "Clean Architecture", "Pearson", "9791861972711");
        saveBook("Joshua Bloch", "Effective Java", "Addison-Wesley", "9784876811090");

        List<Book> result =
                bookRepository.findByAuthorContainingIgnoreCaseAndTitleContainingIgnoreCase("martin", "clean");

        assertEquals(2, result.size());
        assertTrue(
                result.stream().anyMatch(b -> b.getTitle().equals("Clean Code"))
        );
        assertTrue(
                result.stream().anyMatch(b -> b.getTitle().equals("Clean Architecture"))
        );
    }

    @Test
    void findByAuthorContaining_shouldReturnBooksByAuthorFragment() {
        saveBook("Robert Martin", "Clean Code", "Prentice Hall", "9780306406157");
        saveBook("Joshua Bloch", "Effective Java", "Addison-Wesley", "9791861972711");

        List<Book> result = bookRepository.findByAuthorContainingIgnoreCase("martin");

        assertEquals(1, result.size());
        assertTrue(result.getFirst().getAuthor().contains("Martin"));
    }

    @Test
    void findByTitleContaining_shouldReturnBooksByTitleFragment() {
        saveBook("Robert Martin", "Clean Code", "Prentice Hall", "9780306406157");
        saveBook("Robert Martin", "Clean Architecture", "Pearson", "9791861972711");

        List<Book> result = bookRepository.findByTitleContainingIgnoreCase("architecture");

        assertEquals(1, result.size());
        assertEquals("Clean Architecture", result.getFirst().getTitle());
    }

    @Test
    void findByIsbn_shouldReturnBook_whenIsbnExists() {
        saveBook("Joshua Bloch", "Effective Java", "Addison-Wesley", "9784876811090");

        Optional<Book> result = bookRepository.findByIsbn(new Isbn("9784876811090"));

        assertTrue(result.isPresent());
        assertEquals("Effective Java", result.get().getTitle());
    }

    @Test
    void findByIsbn_shouldReturnEmpty_whenIsbnDoesNotExist() {
        Optional<Book> result = bookRepository.findByIsbn(new Isbn("9780306406157"));

        assertFalse(result.isPresent());
    }
}