package com.bhnatiuk.uni.bookstore.backend.service;

public interface TokenService {

    String generateToken(String name);

    boolean isValid(String trimmedToken);

    String getUsername(String token);

    String resolveToken(String header);

    long getExpiration();

    String getType();
}
