package com.bhnatiuk.uni.bookstore.backend.service;

import com.bhnatiuk.uni.bookstore.backend.model.dto.TokenResponse;
import com.bhnatiuk.uni.bookstore.backend.model.dto.UserLoginRequest;
import com.bhnatiuk.uni.bookstore.backend.model.dto.UserRegisterRequest;

public interface AuthService {
    TokenResponse register(UserRegisterRequest registerRequest);

    TokenResponse login(UserLoginRequest loginRequest);
}
