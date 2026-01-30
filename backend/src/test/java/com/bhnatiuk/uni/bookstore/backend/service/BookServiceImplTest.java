package com.bhnatiuk.uni.bookstore.backend.service;

import com.bhnatiuk.uni.bookstore.backend.model.domain.Isbn;
import com.bhnatiuk.uni.bookstore.backend.model.dto.BookCreationRequest;
import com.bhnatiuk.uni.bookstore.backend.model.dto.BookResponse;
import com.bhnatiuk.uni.bookstore.backend.model.entity.Book;
import com.bhnatiuk.uni.bookstore.backend.model.exception.NotFoundException;
import com.bhnatiuk.uni.bookstore.backend.model.exception.ResourceAlreadyExistsException;
import com.bhnatiuk.uni.bookstore.backend.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//TODO: extract recurrent objects
@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void create_shouldSaveBook_whenRequestIsValid() {
        BookCreationRequest bookCreationRequest = new BookCreationRequest(
                "Title", "Author", "Publisher", "9780306406157"
        );

        Book expectedEntity = new Book();
        expectedEntity.setId(1L);
        expectedEntity.setTitle("Title");
        expectedEntity.setAuthor("Author");
        expectedEntity.setPublisher("Publisher");
        expectedEntity.setIsbn(new Isbn("9780306406157"));

        when(bookRepository.save(any(Book.class)))
                .thenReturn(expectedEntity);

        Book actualResponse = bookService.create(bookCreationRequest);

        assertEquals(expectedEntity, actualResponse);

        ArgumentCaptor<Book> userCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(userCaptor.capture());

        Book actualEntity = userCaptor.getValue();
        assertEquals("Title", actualEntity.getTitle());
        assertEquals(new Isbn("9780306406157"), actualEntity.getIsbn());
    }

    @Test
    void create_shouldThrowAlreadyExistsException_whenIsbnIsAlreadyPersisted() {
        BookCreationRequest bookCreationRequest = new BookCreationRequest(
                "Title", "Author", "Publisher", "9780306406157"
        );

        when(bookRepository.existsByIsbn(new Isbn("9780306406157")))
                .thenReturn(true);

        assertThrows(
                ResourceAlreadyExistsException.class,
                () -> bookService.create(bookCreationRequest)
        );
    }

    @Test
    void getByIsbn_shouldReturnBookResponse_whenBookExists() {
        Isbn isbn = new Isbn("9780306406157");
        Book expectedEntity = new Book();
        expectedEntity.setId(1L);
        expectedEntity.setTitle("Title");
        expectedEntity.setAuthor("Author");
        expectedEntity.setPublisher("Publisher");
        expectedEntity.setIsbn(isbn);

        when(bookRepository.findByIsbn(isbn))
                .thenReturn(Optional.of(expectedEntity));

        BookResponse response = bookService.getByIsbn(isbn);

        assertNotNull(response);
        assertEquals("Title", response.title());
        assertEquals(isbn.getValue(), response.isbn());

        verify(bookRepository).findByIsbn(isbn);
    }

    @Test
    void getByIsbn_shouldThrowNotFoundException_whenBookDoesNotExist() {
        Isbn isbn = new Isbn("9780306406157");

        when(bookRepository.findByIsbn(isbn))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> bookService.getByIsbn(isbn)
        );

        assertEquals("Book not found", exception.getMessage());
        verify(bookRepository).findByIsbn(isbn);
    }

    @Test
    void find_shouldFindByAuthorAndTitle_whenBothProvided() {
        String author = "Bloch";
        String title = "Java";

        Book expectedEntity = new Book();
        expectedEntity.setId(1L);
        expectedEntity.setTitle("Effective Java");
        expectedEntity.setAuthor(author);
        expectedEntity.setPublisher("Publisher");
        expectedEntity.setIsbn(new Isbn("9780306406157"));

        when(bookRepository.findByAuthorContainingIgnoreCaseAndTitleContainingIgnoreCase(author, title))
                .thenReturn(List.of(expectedEntity));

        List<BookResponse> result = bookService.find(author, title);

        assertEquals(1, result.size());
        assertEquals("Effective Java", result.getFirst().title());

        verify(bookRepository).findByAuthorContainingIgnoreCaseAndTitleContainingIgnoreCase(author, title);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void shouldFindByAuthor_whenOnlyAuthorProvided() {
        String author = "Bloch";

        Book expectedEntity = new Book();
        expectedEntity.setId(1L);
        expectedEntity.setTitle("Effective Java");
        expectedEntity.setAuthor(author);
        expectedEntity.setPublisher("Publisher");
        expectedEntity.setIsbn(new Isbn("9780306406157"));

        when(bookRepository.findByAuthorContainingIgnoreCase(author))
                .thenReturn(List.of(expectedEntity));

        List<BookResponse> result = bookService.find(author, null);

        assertEquals(1, result.size());
        assertEquals(author, result.getFirst().author());

        verify(bookRepository).findByAuthorContainingIgnoreCase(author);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void shouldFindByTitle_whenOnlyTitleProvided() {
        String title = "Java";

        Book expectedEntity = new Book();
        expectedEntity.setId(1L);
        expectedEntity.setTitle("Effective Java");
        expectedEntity.setAuthor("Bloch");
        expectedEntity.setPublisher("Publisher");
        expectedEntity.setIsbn(new Isbn("9780306406157"));

        when(bookRepository.findByTitleContainingIgnoreCase(title))
                .thenReturn(List.of(expectedEntity));

        List<BookResponse> result = bookService.find(null, title);

        assertEquals(1, result.size());
        assertTrue(result.getFirst().title().contains(title));

        verify(bookRepository).findByTitleContainingIgnoreCase(title);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void shouldFindAll_whenNoFiltersProvided() {
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Effective Java");
        book1.setAuthor("Joshua Bloch");
        book1.setPublisher("Publisher");
        book1.setIsbn(new Isbn("9780306406157"));

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Clean Code");
        book2.setAuthor("Robert Martin");
        book2.setPublisher("Publisher");
        book2.setIsbn(new Isbn("9780132350884"));

        when(bookRepository.findAll())
                .thenReturn(List.of(book1, book2));

        List<BookResponse> result = bookService.find(null, null);

        assertEquals(2, result.size());

        verify(bookRepository).findAll();
        verifyNoMoreInteractions(bookRepository);
    }
}