package com.bhnatiuk.uni.bookstore.backend.controller;

import com.bhnatiuk.uni.bookstore.backend.config.exception.GlobalExceptionHandler;
import com.bhnatiuk.uni.bookstore.backend.config.exception.HttpStatusExceptionMapper;
import com.bhnatiuk.uni.bookstore.backend.config.security.JwtAuthenticationFilter;
import com.bhnatiuk.uni.bookstore.backend.model.domain.Isbn;
import com.bhnatiuk.uni.bookstore.backend.model.dto.BookCreationRequest;
import com.bhnatiuk.uni.bookstore.backend.model.dto.BookResponse;
import com.bhnatiuk.uni.bookstore.backend.model.entity.Book;
import com.bhnatiuk.uni.bookstore.backend.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.function.Function;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = BookController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {JwtAuthenticationFilter.class}
        )
)
@Import({
        GlobalExceptionHandler.class,
        HttpStatusExceptionMapper.class
})
@AutoConfigureMockMvc(addFilters = false)
class BookControllerTest {
    public static final String CREATE_PATH = "/api/books/new";
    public static final String GET_BY_ID_PATH = "/api/books/{isbn}";
    public static final String FIND_PATH = "/api/books";

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler(new HttpStatusExceptionMapper());
    }

    private <T> void testInvalidDtoRequest(T request, String requestPath) throws Exception {
        mockMvc.perform(post(requestPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.path").value(requestPath));
    }

    private static Stream<String> blankStrings() {
        return Stream.of(null, "", " ", "  ");
    }

    private static Stream<String> invalidIsbns() {
        // WARNING! this pool of test entries is only for controller (dto, pathVar) validation
        // ISBN object creation ensures full validation
        return Stream.of(
                // non-digit characters
                "qwerty_qwerty",
                "123456789a",
                "123456789@",

                // separators not allowed by regex
                "978-0-324-73607-6",
                "978 0 324 73607 6",
                "978_0_324_73607_6",

                // wrong length
                "12345678",
                "12345678901",
                "123456789012",

                // misplaced X
                "X123456789",
                "12345678X9"
        );
    }

    @Test
    void createBook_shouldReturnCreatedResponse_whenValidIsbn13Request() throws Exception {
        createBook_testValidRequest("9784876811090", "9784876811090");
    }

    @Test
    void createBook_shouldReturnCreatedResponse_whenValidIsbn10Request() throws Exception {
        createBook_testValidRequest("032473607X", "9780324736076");
    }

    private void createBook_testValidRequest(String requestIsbn, String responseIsbn) throws Exception {
        BookCreationRequest request = new BookCreationRequest(
                "Test title", "Test Author", "Test Publisher", requestIsbn
        );

        Book expectedCreatedEntity = BookCreationRequest.toEntity(request);

        BookResponse expectedResponse = new BookResponse(
                "Test title", "Test Author", "Test Publisher", responseIsbn
        );

        when(bookService.create(request)).thenReturn(expectedCreatedEntity);

        mockMvc.perform(post(CREATE_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json(
                        objectMapper.writeValueAsString(expectedResponse)
                ))
                .andExpect(header().string(
                        "Location", "http://localhost/api/books/" + expectedResponse.isbn()
                ));
    }

    @ParameterizedTest
    @MethodSource("invalidCreationRequestProvider")
    void createBook_shouldReturnBadRequest_whenInvalidCreationRequest(BookCreationRequest request) throws Exception {
        testInvalidDtoRequest(request, CREATE_PATH);
    }

    public static Stream<BookCreationRequest> invalidCreationRequestProvider() {
        return Stream.of(
                invalidTitleRequestProvider(),
                invalidAuthorRequestProvider(),
                invalidPublisherRequestProvider(),
                invalidIsbnRequestProvider()
        ).flatMap(Function.identity());
    }

    public static Stream<BookCreationRequest> invalidTitleRequestProvider() {
        return blankStrings()
                .map(title -> new BookCreationRequest(
                        title, "Test Author", "Test Publisher", "9784876811090"
                ));
    }

    public static Stream<BookCreationRequest> invalidAuthorRequestProvider() {
        return blankStrings()
                .map(author -> new BookCreationRequest(
                        "Test Title", author, "Test Publisher", "9784876811090"
                ));
    }

    public static Stream<BookCreationRequest> invalidPublisherRequestProvider() {
        return blankStrings()
                .map(publisher -> new BookCreationRequest(
                        "Test Title", "Test Author", publisher, "9784876811090"
                ));
    }

    public static Stream<BookCreationRequest> invalidIsbnRequestProvider() {
        Stream<String> invalidAndBlankIsbns = Stream.concat(
                blankStrings(),
                invalidIsbns()
        );
        return invalidAndBlankIsbns
                .map(isbn -> new BookCreationRequest(
                        "Test Title", "Test Author", "Test Publisher", isbn
                ));
    }

    @Test
    void getBookById_shouldReturnBook_byValidIsbn() throws Exception {
        BookResponse response = new BookResponse(
                "Test Title",
                "Test Author",
                "Test Publisher",
                "9780324736076"
        );

        when(bookService.getByIsbn(any(Isbn.class)))
                .thenReturn(response);

        mockMvc.perform(get(GET_BY_ID_PATH, "9780324736076"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        objectMapper.writeValueAsString(response)
                ));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            " ",
            "  "
    })
    @MethodSource("invalidIsbns")
    void getBookById_shouldReturnBadRequest_whenInvalidIsbn(String isbn) throws Exception {
        mockMvc.perform(get(GET_BY_ID_PATH, isbn))
                .andExpect(status().isBadRequest());
    }

    @SuppressWarnings("JUnitMalformedDeclaration")
    @ParameterizedTest
    @NullAndEmptySource
    void getBookById_shouldReturnNotFound_whenNullOrEmptyIsbn(String isbn) throws Exception {
        mockMvc.perform(get(GET_BY_ID_PATH, isbn))
                .andExpect(status().isNotFound());
    }

    //TODO: test find endpoint
}