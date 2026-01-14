package com.bhnatiuk.uni.bookstore.backend.controller;

import com.bhnatiuk.uni.bookstore.backend.dto.UserRegisterRequest;
import com.bhnatiuk.uni.bookstore.backend.dto.UserResponse;
import com.bhnatiuk.uni.bookstore.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/api/auth/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserRegisterRequest registerRequest) {
        return ResponseEntity.ok(
                authService.register(registerRequest)
        );
    }
}
