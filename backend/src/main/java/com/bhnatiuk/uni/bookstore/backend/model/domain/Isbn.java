package com.bhnatiuk.uni.bookstore.backend.model.domain;

import com.bhnatiuk.uni.bookstore.backend.model.exception.InvalidIsbnException;
import lombok.Getter;

@Getter
public final class Isbn {
    private static final int LEGACY_ISBN_LENGTH = 10;
    private static final int MODERN_ISBN_LENGTH = 13;

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
            throw new InvalidIsbnException("value is null or empty");


        if (isbn.length() != LEGACY_ISBN_LENGTH && isbn.length() != MODERN_ISBN_LENGTH)
            throw new InvalidIsbnException("value length is invalid (allowed 10 or 13): " + isbn.length());

        if (isbn.chars().allMatch(c -> c == '0'))
            throw new InvalidIsbnException("value cannot contain only zeros");

        if (isbn.length() == MODERN_ISBN_LENGTH && !isbn.matches("^\\d{13}"))
            throw new InvalidIsbnException("Modern value should contain only digits");

        if (isbn.length() == LEGACY_ISBN_LENGTH && !isbn.matches("^\\d{9}[\\dX]$"))
            throw new InvalidIsbnException("Legacy value should contain only digits and may use X as checksum");

        if (!isValidChecksum(isbn))
            throw new InvalidIsbnException("value checksum is invalid");
    }

    private static String convertToModernIsbn(String isbnString) {
        String modernIsbnWithoutChecksum = "978" + isbnString.substring(0, LEGACY_ISBN_LENGTH - 1);
        return modernIsbnWithoutChecksum + checksumForModernIsbn(modernIsbnWithoutChecksum);
    }

    private static boolean isValidChecksum(String ISBN) {
        String isbnWithoutChecksum = ISBN.substring(0, ISBN.length() - 1);
        char checksum = calculateChecksumChar(isbnWithoutChecksum);
        return checksum == ISBN.charAt(ISBN.length() - 1);
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

        int checksum = 10 - (sum % 10);
        return Character.forDigit(checksum, 10);
    }
}
