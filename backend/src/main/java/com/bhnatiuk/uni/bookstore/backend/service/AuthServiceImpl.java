package com.bhnatiuk.uni.bookstore.backend.service;

import com.bhnatiuk.uni.bookstore.backend.dto.UserRegisterRequest;
import com.bhnatiuk.uni.bookstore.backend.dto.UserResponse;
import com.bhnatiuk.uni.bookstore.backend.entity.AppUser;
import com.bhnatiuk.uni.bookstore.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;

    @Override
    public UserResponse register(UserRegisterRequest registerRequest) {
        AppUser appUser = new AppUser();
        appUser.setUsername(registerRequest.username());
        appUser.setEmail(registerRequest.email());
        appUser.setPassword(registerRequest.password());

        AppUser savedUser = userRepository.save(appUser);

        return new UserResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
    }
}
