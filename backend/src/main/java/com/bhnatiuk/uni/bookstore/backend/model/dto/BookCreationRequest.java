package com.bhnatiuk.uni.bookstore.backend.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record BookCreationRequest(

        @NotBlank
        String title,

        @NotBlank
        String author,

        @NotBlank
        String publisher,

        @Min(1_000_000_000_000L)
        @Max(9_999_999_999_999L)
        Long ISBN
) {
}
