package com.bhnatiuk.uni.bookstore.backend.model.dto;

import jakarta.validation.constraints.NotBlank;

public record TokenResponse(
        @NotBlank String jwtToken,
        @NotBlank String tokenType,
        long expiresIn
) {
}
