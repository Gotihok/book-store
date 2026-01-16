package com.bhnatiuk.uni.bookstore.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record UserRegisterRequest(
        @NotBlank String username,
        @NotBlank String email,
        @NotBlank String password) {
}
