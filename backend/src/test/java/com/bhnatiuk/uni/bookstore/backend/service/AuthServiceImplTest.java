package com.bhnatiuk.uni.bookstore.backend.service;

import com.bhnatiuk.uni.bookstore.backend.dto.UserRegisterRequest;
import com.bhnatiuk.uni.bookstore.backend.dto.UserResponse;
import com.bhnatiuk.uni.bookstore.backend.entity.AppUser;
import com.bhnatiuk.uni.bookstore.backend.repository.UserRepository;
import com.bhnatiuk.uni.bookstore.backend.util.exception.CredentialsAlreadyInUseException;
import com.bhnatiuk.uni.bookstore.backend.util.exception.MalformedEmailException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    private UserRegisterRequest registerRequest;
    private UserResponse expectedResponse;

    @BeforeEach
    void setUp() {
        registerRequest = new UserRegisterRequest(
                "testUsername", "test@mail.com", "testPassword"
        );
        expectedResponse = new UserResponse(
                1L, "testUsername", "test@mail.com"
        );
    }

    @Test
    void register_shouldReturnRegisteredUser_whenCorrectCredentials() {
        // given
        when(userRepository.save(any(AppUser.class)))
                .thenAnswer(invocation -> {
                    AppUser user = invocation.getArgument(0);
                    user.setId(1L);
                    return user;
                });

        when(userRepository.existsByEmail("test@mail.com"))
                .thenReturn(false);
        when(userRepository.existsByUsername("testUsername"))
                .thenReturn(false);

        when(passwordEncoder.encode("testPassword"))
                .thenReturn("encodedPassword");

        // when
        UserResponse actualResponse = authServiceImpl.register(registerRequest);

        // then
        assertEquals(expectedResponse, actualResponse);

        ArgumentCaptor<AppUser> userCaptor = ArgumentCaptor.forClass(AppUser.class);
        verify(userRepository).save(userCaptor.capture());

        AppUser savedUser = userCaptor.getValue();
        assertNotNull(savedUser);
        assertEquals(1L, savedUser.getId());
        assertEquals("testUsername", savedUser.getUsername());
        assertEquals("test@mail.com", savedUser.getEmail());
        assertEquals("encodedPassword", savedUser.getPassword());
    }

    @Test
    void register_shouldThrowCredentialsException_whenEmailAlreadyInUse() {
        when(userRepository.existsByEmail("test@mail.com"))
                .thenReturn(true);

        assertThrows(
                CredentialsAlreadyInUseException.class,
                () -> authServiceImpl.register(registerRequest)
        );
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_shouldThrowCredentialsException_whenUsernameAlreadyInUse() {
        when(userRepository.existsByUsername("testUsername"))
                .thenReturn(true);

        assertThrows(
                CredentialsAlreadyInUseException.class,
                () -> authServiceImpl.register(registerRequest)
        );
        verify(userRepository, never()).save(any());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "missingDomain",
            "@missingusername.com",
            "missingDomain@.com",
            "missingAtSign.com"
    })
    void register_shouldThrowMalformedEmailException_whenEmailIsMalformed(String email) {
        registerRequest = new UserRegisterRequest(
                "testUsername", email, "testPassword"
        );

        assertThrows(
                MalformedEmailException.class,
                () -> authServiceImpl.register(registerRequest)
        );
        verify(userRepository, never()).save(any());
    }

    @ParameterizedTest
    @MethodSource("invalidRegisterRequests")
    void register_shouldThrowIllegalArgumentException_whenRegisterRequestIsNullEmptyOrIncomplete(
            UserRegisterRequest emptyRegisterRequest) {
        assertThrows(
                IllegalArgumentException.class,
                () -> authServiceImpl.register(emptyRegisterRequest)
        );
        verify(userRepository, never()).save(any());
    }

    static  Stream<UserRegisterRequest> invalidRegisterRequests() {
        return Stream.of(
                null,
                new UserRegisterRequest(null, null, null),

                new UserRegisterRequest(null, "test@mail.com", "testPassword"),
                new UserRegisterRequest("", "test@mail.com", "testPassword"),
                new UserRegisterRequest("  ", "test@mail.com", "testPassword"),

                new UserRegisterRequest("testUsername", null, "testPassword"),
                new UserRegisterRequest("testUsername", "", "testPassword"),
                new UserRegisterRequest("testUsername", " ", "testPassword"),

                new UserRegisterRequest("testUsername", "test@mail.com",  null),
                new UserRegisterRequest("testUsername", "test@mail.com", ""),
                new UserRegisterRequest("testUsername", "test@mail.com", "   ")
        );
    }
}