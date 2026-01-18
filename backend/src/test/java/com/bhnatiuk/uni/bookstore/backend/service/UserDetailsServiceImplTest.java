package com.bhnatiuk.uni.bookstore.backend.service;

import com.bhnatiuk.uni.bookstore.backend.model.entity.AppUser;
import com.bhnatiuk.uni.bookstore.backend.model.exception.NotFoundException;
import com.bhnatiuk.uni.bookstore.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {
    @Mock
    private UserRepository userRepository;

    private UserDetailsServiceImpl userDetailsServiceImpl;

    @BeforeEach
    void setUp() {
        userDetailsServiceImpl = new UserDetailsServiceImpl(userRepository);
    }

    @Test
    void loadUserByUsername_shouldReturnUser_whenUserFound() {
        AppUser testUser = new AppUser();
        testUser.setId(1L);
        testUser.setUsername("testUsername");
        testUser.setEmail("test@mail.com");
        testUser.setPassword("testPassword");

        when(userRepository.findAppUserByUsername("testUsername"))
                .thenReturn(Optional.of(testUser));

        UserDetails retrievedUser =
                userDetailsServiceImpl.loadUserByUsername("testUsername");

        assertEquals(testUser, retrievedUser);
        verify(userRepository).findAppUserByUsername("testUsername");
    }

    @Test
    void loadUserByUsername_shouldThrowNotFoundException_whenUserNotFound() {
        when(userRepository.findAppUserByUsername("testUsername"))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> userDetailsServiceImpl.loadUserByUsername("testUsername"));

        verify(userRepository).findAppUserByUsername("testUsername");
    }
}