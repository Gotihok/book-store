package com.bhnatiuk.uni.bookstore.backend.dto;

import java.io.Serializable;

public record UserResponse(Long id, String username, String email) {
}
