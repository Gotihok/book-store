package com.bhnatiuk.uni.bookstore.backend.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserRegisterRequest(

        @NotBlank
        @Pattern(regexp = "^[a-zA-Z0-9._-]+$")
        String username,

        @NotBlank
        @Email(regexp = ALLOWED_EMAIL_PATTERN_REGEX)
        String email,

        // TODO: create weak password verification
        @NotBlank
        String password

) {
    private static final String ALLOWED_EMAIL_PATTERN_REGEX = "^(\\S+)@(\\S+\\.\\S+)$";
}
