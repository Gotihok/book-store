package com.bhnatiuk.uni.bookstore.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record UserResponse(
        @NotBlank Long id,
        @NotBlank String username,
        @NotBlank String email) {
}
