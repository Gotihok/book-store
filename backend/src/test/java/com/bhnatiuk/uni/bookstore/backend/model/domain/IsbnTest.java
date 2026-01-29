package com.bhnatiuk.uni.bookstore.backend.model.domain;

import com.bhnatiuk.uni.bookstore.backend.model.exception.InvalidIsbnException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class IsbnTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "9780306406157",
            "9791861972711",
            "9784876811090",
    })
    void constructor_shouldInstantiate_whenIsbn13IsValid(String isbn13String) {
        Isbn isbnObject = new Isbn(isbn13String);

        assertEquals(isbn13String, isbnObject.getValue());
    }

    @Test
    void constructor_shouldInstantiateAndTransformToIsbn13_whenIsbn10IsOnlyValidNumbers() {
        String inputIsbn10String = "1306406153";
        String expectedIsbn13String = "9781306406154";

        Isbn isbnObject = new Isbn(inputIsbn10String);

        assertEquals(expectedIsbn13String, isbnObject.getValue());
    }

    @Test
    void constructor_shouldInstantiateAndTransformToIsbn13_whenIsbn10IsValidWithChecksumX() {
        String inputIsbn10String = "385197252X";
        String expectedIsbn13String = "9783851972528";

        Isbn isbnObject = new Isbn(inputIsbn10String);

        assertEquals(expectedIsbn13String, isbnObject.getValue());
    }

    @Test
    void constructor_shouldInstantiateAndTransformToIsbn13_whenIsbn10IsValidWithChecksumLowercaseX() {
        String inputIsbn10String = "385197252x";
        String expectedIsbn13String = "9783851972528";

        Isbn isbnObject = new Isbn(inputIsbn10String);

        assertEquals(expectedIsbn13String, isbnObject.getValue());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
            " ",                    // whitespace only
            "   ",                  // multiple spaces
            "text_in_isbn",         // cannot contain letters
            "1234abcd56789",        // mixed letters & digits
            "123-456-789-0123",     // separators not allowed
            "123 456 789 0123",     // spaces not allowed
            "0000000000000",        // cannot be only zeros

            "123456789",            // invalid length (9)
            "123456789012",         // invalid length (12)
            "12345678901234",       // invalid length (14)

            "9781234567890",        // invalid checksum (x13)
            "978123456789X",        // invalid checksum character (x13)
            "123456788X",           // invalid checksum (x10)
            "12345678X8",           // invalid checksum position (x10)
            "0471958695",           // invalid checksum (x10)

            "4731861972712",        // not an isbn, because of invalid EAN prefix
    })
    void constructor_shouldThrowException_whenIsbnFormatIsInvalid(String isbnString) {
        assertThrows(
                InvalidIsbnException.class,
                () -> new Isbn(isbnString)
        );
    }

    @Test
    void equals_shouldReturnTrue_whenIsbnValuesAreEqual() {
        Isbn isbn1 = new Isbn("9780306406157");
        Isbn isbn2 = new Isbn("9780306406157");

        boolean isEqual = isbn1.equals(isbn2);

        assertTrue(isEqual);
    }

    @Test
    void equals_shouldReturnFalse_whenIsbnValuesAreNotEqual() {
        Isbn isbn1 = new Isbn("9780306406157");
        Isbn isbn2 = new Isbn("9791861972711");

        boolean isEqual = isbn1.equals(isbn2);

        assertFalse(isEqual);
    }
}