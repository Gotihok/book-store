package com.bhnatiuk.uni.bookstore.backend.dto;

//TODO: remove isEmptyOrIncomplete method
// and create global bean validation of all DTO records
// that require that, with @NotBlank
public record UserRegisterRequest(String username, String email, String password) {
    public boolean isEmptyOrIncomplete() {
        return username == null || email == null || password == null
                || username.isBlank() || email.isBlank() || password.isBlank();
    }
}
