package com.bhnatiuk.uni.bookstore.backend.service;

import com.bhnatiuk.uni.bookstore.backend.model.domain.Isbn;
import com.bhnatiuk.uni.bookstore.backend.model.dto.BookCreationRequest;
import com.bhnatiuk.uni.bookstore.backend.model.dto.BookResponse;
import com.bhnatiuk.uni.bookstore.backend.model.dto.BookUpdateRequest;
import com.bhnatiuk.uni.bookstore.backend.model.entity.Book;
import com.bhnatiuk.uni.bookstore.backend.model.exception.NotFoundException;
import com.bhnatiuk.uni.bookstore.backend.model.exception.ResourceAlreadyExistsException;
import com.bhnatiuk.uni.bookstore.backend.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

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
    void find_shouldFindByAuthor_whenOnlyAuthorProvided() {
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
    void find_shouldFindByTitle_whenOnlyTitleProvided() {
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
    void find_shouldFindAll_whenNoFiltersProvided() {
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

    //TODO: test update by isbn

    static Stream<String> blanks() {
        return Stream.of(null, "", " ");
    }

    static Stream<Arguments> updateRequests() {
        Stream<Arguments> withBlanks = Stream.of(
                // title only
                blanks().map(b ->
                        Arguments.of(
                                new BookUpdateRequest("New title", b, b),
                                "New title", "Old author", "Old publisher"
                        )
                ),

                // author only
                blanks().map(b ->
                        Arguments.of(
                                new BookUpdateRequest(b, "New author", b),
                                "Old title", "New author", "Old publisher"
                        )
                ),

                // publisher only
                blanks().map(b ->
                        Arguments.of(
                                new BookUpdateRequest(b, b, "New publisher"),
                                "Old title", "Old author", "New publisher"
                        )
                ),

                // title and author
                blanks().map(b ->
                        Arguments.of(
                                new BookUpdateRequest("New title", "New author", b),
                                "New title", "New author", "Old publisher"
                        )
                ),

                // title and publisher
                blanks().map(b ->
                        Arguments.of(
                                new BookUpdateRequest("New title", b, "New publisher"),
                                "New title", "Old author", "New publisher"
                        )
                ),

                // author and publisher
                blanks().map(b ->
                        Arguments.of(
                                new BookUpdateRequest(b, "New author", "New publisher"),
                                "Old title", "New author", "New publisher"
                        )
                )
        ).flatMap(Function.identity());

        return Stream.concat(
                withBlanks,
                Stream.of(Arguments.of(
                        new BookUpdateRequest("New title", "New author", "New publisher"),
                        "New title", "New author", "New publisher"
                )));
    }

    @ParameterizedTest
    @MethodSource("updateRequests")
    void updateByIsbn_shouldUpdateAndReturnBook_whenExistentIsbn(
            BookUpdateRequest request,
            String expTitle,
            String expAuthor,
            String expPublisher
    ) {
        Isbn isbn = new Isbn("9780132350884");

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Old title");
        book.setAuthor("Old author");
        book.setPublisher("Old publisher");
        book.setIsbn(isbn);

        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(book));
        when(bookRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        BookResponse res = bookService.updateByIsbn(isbn, request);

        assertEquals(res.title(), expTitle);
        assertEquals(res.author(), expAuthor);
        assertEquals(res.publisher(), expPublisher);

        verify(bookRepository).save(argThat(
                saved -> saved.getTitle().equals(expTitle) &&
                        saved.getAuthor().equals(expAuthor) &&
                        saved.getPublisher().equals(expPublisher)
        ));
    }

    @Test
    void updateByIsbn_shouldThrowNotFoundException_whenBookNotFound() {
        Isbn isbn = new Isbn("9780132350884");
        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> bookService.updateByIsbn(
                        isbn, new BookUpdateRequest("New title", "New author", "New publisher"))
        );
        verify(bookRepository, never()).save(any());
    }

    @Test
    void deleteByIsbn_shouldDeleteAndReturnBook_whenExistentIsbn() {
        Isbn isbn = new Isbn("9780132350884");
        BookResponse expectedResponse = new BookResponse(
                "Title", "Author", "Publisher", isbn.getValue()
        );

        Book expectedEntity = new Book();
        expectedEntity.setId(1L);
        expectedEntity.setTitle(expectedResponse.title());
        expectedEntity.setAuthor(expectedResponse.author());
        expectedEntity.setPublisher(expectedResponse.publisher());
        expectedEntity.setIsbn(isbn);

        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(expectedEntity));

        BookResponse actualResponse = bookService.deleteByIsbn(isbn);

        assertEquals(expectedResponse, actualResponse);
        verify(bookRepository).delete(expectedEntity);
    }

    @Test
    void deleteByTitle_shouldThrowNotFoundException_whenUnexistentIsbn() {
        Isbn isbn = new Isbn("9780132350884");
        when(bookRepository.findByIsbn(isbn))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookService.deleteByIsbn(isbn));
        verify(bookRepository, never()).delete(any(Book.class));
    }
}