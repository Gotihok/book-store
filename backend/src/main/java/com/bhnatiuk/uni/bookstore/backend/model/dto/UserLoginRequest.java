package com.bhnatiuk.uni.bookstore.backend.model.dto;

import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest(
        @NotBlank String username,
        @NotBlank String password) {
}
