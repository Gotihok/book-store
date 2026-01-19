package com.bhnatiuk.uni.bookstore.backend.service;

import com.bhnatiuk.uni.bookstore.backend.model.dto.TokenResponse;
import com.bhnatiuk.uni.bookstore.backend.model.dto.UserLoginRequest;
import com.bhnatiuk.uni.bookstore.backend.model.dto.UserRegisterRequest;
import com.bhnatiuk.uni.bookstore.backend.model.dto.UserResponse;
import com.bhnatiuk.uni.bookstore.backend.model.entity.AppUser;
import com.bhnatiuk.uni.bookstore.backend.repository.UserRepository;
import com.bhnatiuk.uni.bookstore.backend.model.exception.CredentialsAlreadyInUseException;
import com.bhnatiuk.uni.bookstore.backend.model.exception.MalformedEmailException;
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
//    private static final String ALLOWED_EMAIL_PATTERN_REGEX = "^(\\S+)@(\\S+\\.\\S+)$";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    // TODO: extract user validation logic to separate class (Maybe later)
    // TODO: create weak password verification
    // TODO: adjust to url creation
    // TODO: test properly (especially every exception)
    @Override
    public UserResponse register(@NotNull UserRegisterRequest registerRequest) {
//        if (!registerRequest.email().matches(ALLOWED_EMAIL_PATTERN_REGEX)) {
//            throw new MalformedEmailException("Malformed email provided for registration");
//        }

        // TODO: make 1 db call out of 2
        if (userRepository.existsByEmail(registerRequest.email())
                || userRepository.existsByUsername(registerRequest.username())
        ) {
            throw new CredentialsAlreadyInUseException("Provided credentials already exist");
        }

        // TODO: extract mapping and entity creation
        AppUser appUser = new AppUser();
        appUser.setUsername(registerRequest.username());
        appUser.setEmail(registerRequest.email());
        appUser.setPassword(passwordEncoder.encode(registerRequest.password()));

        AppUser savedUser = userRepository.save(appUser);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail()
        );
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
