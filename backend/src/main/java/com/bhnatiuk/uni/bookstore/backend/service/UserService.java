package com.bhnatiuk.uni.bookstore.backend.service;

import com.bhnatiuk.uni.bookstore.backend.model.dto.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();
}
