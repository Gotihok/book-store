package com.bhnatiuk.uni.bookstore.backend.service;

import com.bhnatiuk.uni.bookstore.backend.dto.TokenResponse;
import com.bhnatiuk.uni.bookstore.backend.dto.UserLoginRequest;
import com.bhnatiuk.uni.bookstore.backend.dto.UserRegisterRequest;
import com.bhnatiuk.uni.bookstore.backend.dto.UserResponse;

public interface AuthService {
    UserResponse register(UserRegisterRequest registerRequest);

    TokenResponse login(UserLoginRequest loginRequest);
}
