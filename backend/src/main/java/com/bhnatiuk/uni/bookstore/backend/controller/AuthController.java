package com.bhnatiuk.uni.bookstore.backend.controller;

import com.bhnatiuk.uni.bookstore.backend.dto.TokenResponse;
import com.bhnatiuk.uni.bookstore.backend.dto.UserLoginRequest;
import com.bhnatiuk.uni.bookstore.backend.dto.UserRegisterRequest;
import com.bhnatiuk.uni.bookstore.backend.dto.UserResponse;
import com.bhnatiuk.uni.bookstore.backend.service.AuthService;
import jakarta.validation.Valid;
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
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRegisterRequest registerRequest) {
        //TODO: change status to created with redirect URL issuing
        return ResponseEntity.ok(
                authService.register(registerRequest)
        );
    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody UserLoginRequest loginRequest) {
        return ResponseEntity.ok(
                authService.login(loginRequest)
        );
    }
}
