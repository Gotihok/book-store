package com.bhnatiuk.uni.bookstore.backend.service;

import com.bhnatiuk.uni.bookstore.backend.dto.UserRegisterRequest;
import com.bhnatiuk.uni.bookstore.backend.dto.UserResponse;
import com.bhnatiuk.uni.bookstore.backend.entity.AppUser;
import com.bhnatiuk.uni.bookstore.backend.repository.UserRepository;
import com.bhnatiuk.uni.bookstore.backend.util.exception.CredentialsAlreadyInUseException;
import com.bhnatiuk.uni.bookstore.backend.util.exception.MalformedEmailException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private static final String ALLOWED_EMAIL_PATTERN_REGEX = "^(\\S+)@(.+\\..+)$";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse register(UserRegisterRequest registerRequest) {
        if (registerRequest == null || registerRequest.isEmptyOrIncomplete()) {
            throw new IllegalArgumentException("Malformed register request");
        }

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
}
