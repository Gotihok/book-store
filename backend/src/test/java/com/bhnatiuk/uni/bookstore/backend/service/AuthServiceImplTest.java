package com.bhnatiuk.uni.bookstore.backend.service;

import com.bhnatiuk.uni.bookstore.backend.dto.TokenResponse;
import com.bhnatiuk.uni.bookstore.backend.dto.UserLoginRequest;
import com.bhnatiuk.uni.bookstore.backend.dto.UserRegisterRequest;
import com.bhnatiuk.uni.bookstore.backend.dto.UserResponse;
import com.bhnatiuk.uni.bookstore.backend.entity.AppUser;
import com.bhnatiuk.uni.bookstore.backend.repository.UserRepository;
import com.bhnatiuk.uni.bookstore.backend.util.exception.service.CredentialsAlreadyInUseException;
import com.bhnatiuk.uni.bookstore.backend.util.exception.service.MalformedEmailException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    private UserRegisterRequest registerRequest;
    private UserResponse expectedResponse;

    private UserLoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new UserRegisterRequest(
                "testUsername", "test@mail.com", "testPassword"
        );
        expectedResponse = new UserResponse(
                1L, "testUsername", "test@mail.com"
        );

        loginRequest = new UserLoginRequest(
                "testUsername", "testPassword"
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
            "whitespaces in username1@mail.com",
            "whitespacesInUsername2 @mail.com",
            "username@whitespace in domain1.com",
            "username@mail.whitespace in domain2",
            "username@mail. whitespaceInDomain3",
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

    @Test
    void login_shouldAuthenticateUser_whenCorrectCredentials() {
        //given
        Authentication authentication = mock(Authentication.class);
        TokenResponse expectedTokenResponse = new TokenResponse(
                "test.jwt.token",
                "Bearer ",
                1000 * 60
        );

        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUsername");
        when(tokenService.generateToken("testUsername")).thenReturn(expectedTokenResponse.jwtToken());
        when(tokenService.getType()).thenReturn(expectedTokenResponse.tokenType());
        when(tokenService.getExpiration()).thenReturn(expectedTokenResponse.expiresIn());

        TokenResponse actualTokenResponse = authServiceImpl.login(loginRequest);

        assertNotNull(actualTokenResponse);
        assertEquals(expectedTokenResponse.jwtToken(), actualTokenResponse.jwtToken());
        assertEquals(expectedTokenResponse.tokenType(), actualTokenResponse.tokenType());
        assertEquals(expectedTokenResponse.expiresIn(), actualTokenResponse.expiresIn());

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken("testUsername", "testPassword")
        );
        verify(tokenService).generateToken("testUsername");
    }

    @Test
    void login_shouldThrowAuthenticationException_whenAuthenticationFails() {
        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenThrow(UsernameNotFoundException.class);

        assertThrows(AuthenticationException.class, () -> authServiceImpl.login(loginRequest));
        verify(tokenService, never()).generateToken(any());
    }
}