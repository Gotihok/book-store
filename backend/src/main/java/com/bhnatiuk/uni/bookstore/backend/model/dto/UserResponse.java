package com.bhnatiuk.uni.bookstore.backend.model.dto;

import jakarta.validation.constraints.NotBlank;

public record UserResponse(
        @NotBlank Long id,
        @NotBlank String username,
        @NotBlank String email) {
}
