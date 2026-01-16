package com.bhnatiuk.uni.bookstore.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record JwtResponse(
        @NotBlank String jwtToken
) {
}
