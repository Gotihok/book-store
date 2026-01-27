package com.bhnatiuk.uni.bookstore.backend.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record BookCreationRequest(

        @NotBlank
        String title,

        @NotBlank
        String author,

        @NotBlank
        String publisher,

        @Pattern(regexp = "^(\\d{9}[\\dX]|97[89]\\d{10})$")
        String ISBN
) {
}
