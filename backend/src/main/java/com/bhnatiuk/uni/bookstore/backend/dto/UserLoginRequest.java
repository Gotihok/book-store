package com.bhnatiuk.uni.bookstore.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest(
        @NotBlank String username,
        @NotBlank String password) {
}
