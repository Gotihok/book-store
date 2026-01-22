package com.bhnatiuk.uni.bookstore.backend.service;

import com.bhnatiuk.uni.bookstore.backend.model.dto.TokenResponse;
import com.bhnatiuk.uni.bookstore.backend.model.dto.UserLoginRequest;
import com.bhnatiuk.uni.bookstore.backend.model.dto.UserRegisterRequest;
import com.bhnatiuk.uni.bookstore.backend.model.entity.AppUser;
import com.bhnatiuk.uni.bookstore.backend.model.exception.CredentialsAlreadyInUseException;
import com.bhnatiuk.uni.bookstore.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
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

    @Transactional
    @Override
    public TokenResponse register(@NotNull UserRegisterRequest registerRequest) {
        if (userRepository.existsByEmailOrUsername(
                registerRequest.email(), registerRequest.username()
        )) {
            throw new CredentialsAlreadyInUseException("Provided credentials already exist");
        }

        AppUser appUser = new AppUser();
        appUser.setUsername(registerRequest.username());
        appUser.setEmail(registerRequest.email());
        appUser.setPassword(passwordEncoder.encode(registerRequest.password()));

        userRepository.save(appUser);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        registerRequest.username(),
                        registerRequest.password()
                ));

        return buildTokenResponse(authentication);
    }

    @Transactional
    @Override
    public TokenResponse login(@NotNull UserLoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()
                ));

        return buildTokenResponse(authentication);
    }

    private TokenResponse buildTokenResponse(Authentication authentication) {
        return new TokenResponse(
                tokenService.generateToken(authentication.getName()),
                tokenService.getType(),
                tokenService.getExpiration()
        );
    }
}
