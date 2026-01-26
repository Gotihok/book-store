package com.bhnatiuk.uni.bookstore.backend.controller;

import com.bhnatiuk.uni.bookstore.backend.model.dto.TokenResponse;
import com.bhnatiuk.uni.bookstore.backend.model.dto.UserLoginRequest;
import com.bhnatiuk.uni.bookstore.backend.model.dto.UserRegisterRequest;
import com.bhnatiuk.uni.bookstore.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping(
            value = "/api/auth/register",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<TokenResponse> register(@Valid @RequestBody UserRegisterRequest registerRequest) {
//        TokenResponse tokenResponse = authService.register(registerRequest);
//
//        URI location = ServletUriComponentsBuilder
//                .fromCurrentContextPath()
//                .path("/api/auth/me")
//                .build()
//                .toUri();

        return ResponseEntity.ok(
                authService.register(registerRequest)
        );
    }

    // TODO: handle login requests for unexistent users and return meaningful status code
    @PostMapping("/api/auth/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody UserLoginRequest loginRequest) {
        return ResponseEntity.ok(
                authService.login(loginRequest)
        );
    }
}
