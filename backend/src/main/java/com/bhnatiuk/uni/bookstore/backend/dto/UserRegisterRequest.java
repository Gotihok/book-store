package com.bhnatiuk.uni.bookstore.backend.dto;

public record UserRegisterRequest(String username, String email, String password) {
    public boolean isEmptyOrIncomplete() {
        return username == null || email == null || password == null
                || username.isBlank() || email.isBlank() || password.isBlank();
    }
}
