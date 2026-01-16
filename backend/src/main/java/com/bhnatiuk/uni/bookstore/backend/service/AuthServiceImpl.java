package com.bhnatiuk.uni.bookstore.backend.service;

import com.bhnatiuk.uni.bookstore.backend.dto.JwtResponse;
import com.bhnatiuk.uni.bookstore.backend.dto.UserLoginRequest;
import com.bhnatiuk.uni.bookstore.backend.dto.UserRegisterRequest;
import com.bhnatiuk.uni.bookstore.backend.dto.UserResponse;
import com.bhnatiuk.uni.bookstore.backend.entity.AppUser;
import com.bhnatiuk.uni.bookstore.backend.repository.UserRepository;
import com.bhnatiuk.uni.bookstore.backend.util.exception.CredentialsAlreadyInUseException;
import com.bhnatiuk.uni.bookstore.backend.util.exception.LoginFailedException;
import com.bhnatiuk.uni.bookstore.backend.util.exception.MalformedEmailException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private static final String ALLOWED_EMAIL_PATTERN_REGEX = "^(\\S+)@(\\S+\\.\\S+)$";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    @Override
    public UserResponse register(@NotNull UserRegisterRequest registerRequest) {
        if (!registerRequest.email().matches(ALLOWED_EMAIL_PATTERN_REGEX)) {
            throw new MalformedEmailException("Malformed email provided for registration");
        }

        if (userRepository.existsByEmail(registerRequest.email())
                || userRepository.existsByUsername(registerRequest.username())
        ) {
            throw new CredentialsAlreadyInUseException("Provided credentials already exist");
        }

        AppUser appUser = new AppUser();
        appUser.setUsername(registerRequest.username());
        appUser.setEmail(registerRequest.email());
        appUser.setPassword(passwordEncoder.encode(registerRequest.password()));

        AppUser savedUser = userRepository.save(appUser);

        return new UserResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
    }

    @Override
    public JwtResponse login(@NotNull UserLoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
            );
        } catch (AuthenticationException e) {
            throw new LoginFailedException("Login failed");
        }
        String jwt = tokenService.generateToken(authentication.getName());
        return new JwtResponse(jwt);
    }
}
