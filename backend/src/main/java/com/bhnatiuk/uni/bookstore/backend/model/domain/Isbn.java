package com.bhnatiuk.uni.bookstore.backend.model.domain;

import com.bhnatiuk.uni.bookstore.backend.model.exception.InvalidIsbnException;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
public final class Isbn {
    private static final int LEGACY_ISBN_LENGTH = 10;
    private static final int MODERN_ISBN_LENGTH = 13;

    private static final String[] ALLOWED_EAN_PREFIXES = {
            "978",
            "979"
    };

    private final String value;

    public Isbn(String isbnString) {
        validate(isbnString);

        if (isbnString.length() == MODERN_ISBN_LENGTH) {
            this.value = isbnString;
        } else {
            this.value = convertToModernIsbn(isbnString);
        }
    }

    private static void validate(String isbn) {
        if (isbn == null || isbn.isBlank())
            throw new InvalidIsbnException("ISBN is null or empty");

        if (isbn.length() != LEGACY_ISBN_LENGTH && isbn.length() != MODERN_ISBN_LENGTH)
            throw new InvalidIsbnException("ISBN length is invalid (allowed 10 or 13): " + isbn.length());

        if (isbn.length() == MODERN_ISBN_LENGTH && Arrays.stream(ALLOWED_EAN_PREFIXES).noneMatch(isbn::startsWith))
            throw new InvalidIsbnException("Invalid ISBN prefix. Allowed: " + Arrays.toString(ALLOWED_EAN_PREFIXES));

        if (isbn.length() == MODERN_ISBN_LENGTH && !isbn.matches("^\\d{13}"))
            throw new InvalidIsbnException("Modern ISBN should contain only digits");

        if (isbn.length() == LEGACY_ISBN_LENGTH && !isbn.matches("^\\d{9}[\\dX]$"))
            throw new InvalidIsbnException("Legacy ISBN should contain only digits and may use X as checksum");

        if (!isValidChecksum(isbn))
            throw new InvalidIsbnException("ISBN checksum is invalid");
    }

    private static String convertToModernIsbn(String isbnString) {
        String modernIsbnWithoutChecksum = "978" + isbnString.substring(0, LEGACY_ISBN_LENGTH - 1);
        return modernIsbnWithoutChecksum + checksumForModernIsbn(modernIsbnWithoutChecksum);
    }

    private static boolean isValidChecksum(String ISBN) {
        String isbnWithoutChecksum = ISBN.substring(0, ISBN.length() - 1);
        char expectedChecksum = calculateChecksumChar(isbnWithoutChecksum);
        return expectedChecksum == ISBN.charAt(ISBN.length() - 1);
    }

    private static char calculateChecksumChar(String isbnWithoutChecksum) {
        if (isbnWithoutChecksum.length() == MODERN_ISBN_LENGTH - 1) {
            return checksumForModernIsbn(isbnWithoutChecksum);
        } else if (isbnWithoutChecksum.length() == LEGACY_ISBN_LENGTH - 1) {
            return checksumForLegacyIsbn(isbnWithoutChecksum);
        } else {
            throw new InvalidIsbnException(
                    "value length is invalid (allowed 10 or 13): " + isbnWithoutChecksum.length() + 1
            );
        }
    }

    private static char checksumForLegacyIsbn(String isbnWithoutChecksum) {
        int sum = 0;

        for (int i = 0; i < isbnWithoutChecksum.length(); i++) {
            int digit = Character.getNumericValue(isbnWithoutChecksum.charAt(i));
            sum += digit * (10 - i);
        }

        int checksum = (11 - (sum % 11)) % 11;
        return checksum == 10 ? 'X' : Character.forDigit(checksum, 10);
    }

    private static char checksumForModernIsbn(String isbnWithoutChecksum) {
        int sum = 0;

        for (int i = 0; i < isbnWithoutChecksum.length(); i++) {
            int digit = Character.getNumericValue(isbnWithoutChecksum.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }

        int checksum = (10 - (sum % 10)) % 10;
        return Character.forDigit(checksum, 10);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Isbn isbn = (Isbn) o;
        return Objects.equals(value, isbn.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
