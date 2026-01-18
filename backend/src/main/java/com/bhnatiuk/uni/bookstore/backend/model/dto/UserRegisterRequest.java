package com.bhnatiuk.uni.bookstore.backend.model.dto;

import jakarta.validation.constraints.NotBlank;

public record UserRegisterRequest(
        @NotBlank String username,
        @NotBlank String email,
        @NotBlank String password) {
}
