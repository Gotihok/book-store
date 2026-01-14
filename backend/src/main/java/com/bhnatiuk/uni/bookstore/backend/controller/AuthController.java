package com.bhnatiuk.uni.bookstore.backend.controller;

import com.bhnatiuk.uni.bookstore.backend.dto.JwtResponse;
import com.bhnatiuk.uni.bookstore.backend.dto.UserLoginRequest;
import com.bhnatiuk.uni.bookstore.backend.dto.UserRegisterRequest;
import com.bhnatiuk.uni.bookstore.backend.dto.UserResponse;
import com.bhnatiuk.uni.bookstore.backend.service.AuthService;
import com.bhnatiuk.uni.bookstore.backend.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/api/auth/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserRegisterRequest registerRequest) {
        return ResponseEntity.ok(
                authService.register(registerRequest)
        );
    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<JwtResponse> login(@RequestBody UserLoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(), loginRequest.password()
                )
        );

        String jwt = tokenService.generateToken(authentication.getName());
        return ResponseEntity.ok(new JwtResponse(jwt));
    }
}
