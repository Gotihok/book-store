package com.bhnatiuk.uni.bookstore.backend.service;

import com.bhnatiuk.uni.bookstore.backend.model.dto.TokenResponse;
import com.bhnatiuk.uni.bookstore.backend.model.dto.UserLoginRequest;
import com.bhnatiuk.uni.bookstore.backend.model.dto.UserRegisterRequest;
import com.bhnatiuk.uni.bookstore.backend.model.entity.AppUser;
import com.bhnatiuk.uni.bookstore.backend.model.exception.CredentialsAlreadyInUseException;
import com.bhnatiuk.uni.bookstore.backend.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AppUser register(@NotNull UserRegisterRequest registerRequest) {
        if (userRepository.existsByEmailOrUsername(
                registerRequest.email(), registerRequest.username()
        )) {
            throw new CredentialsAlreadyInUseException("Provided credentials already exist");
        }

        AppUser appUser = new AppUser();
        appUser.setUsername(registerRequest.username());
        appUser.setEmail(registerRequest.email());
        appUser.setPassword(passwordEncoder.encode(registerRequest.password()));

        return userRepository.save(appUser);
    }

    @Override
    public TokenResponse login(@NotNull UserLoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
        );

        return new TokenResponse(
                tokenService.generateToken(authentication.getName()),
                tokenService.getType(),
                tokenService.getExpiration()
        );
    }
}
