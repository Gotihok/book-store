package com.bhnatiuk.uni.bookstore.backend.service;

import com.bhnatiuk.uni.bookstore.backend.model.dto.TokenResponse;
import com.bhnatiuk.uni.bookstore.backend.model.dto.UserLoginRequest;
import com.bhnatiuk.uni.bookstore.backend.model.dto.UserRegisterRequest;
import com.bhnatiuk.uni.bookstore.backend.model.entity.AppUser;
import com.bhnatiuk.uni.bookstore.backend.model.exception.CredentialsAlreadyInUseException;
import com.bhnatiuk.uni.bookstore.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
    private AppUser expectedSavedUser;

    private UserLoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new UserRegisterRequest(
                "testUsername", "test@mail.com", "testPassword"
        );
        expectedSavedUser = new AppUser(
                1L, "testUsername", "test@mail.com",  "encodedPassword"
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

        when(userRepository.existsByEmailOrUsername(any(String.class), any(String.class)))
                .thenReturn(false);

        when(passwordEncoder.encode(registerRequest.password()))
                .thenReturn(expectedSavedUser.getPassword());

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

        // when
        TokenResponse actualTokenResponse = authServiceImpl.register(registerRequest);

        // then
        assertEquals(expectedTokenResponse, actualTokenResponse);

        ArgumentCaptor<AppUser> userCaptor = ArgumentCaptor.forClass(AppUser.class);
        verify(userRepository).save(userCaptor.capture());

        AppUser actualSavedUser = userCaptor.getValue();
        assertEquals(expectedSavedUser, actualSavedUser);
    }

    @Test
    void register_shouldThrowCredentialsException_whenEmailOrUsernameAlreadyInUse() {
        when(userRepository.existsByEmailOrUsername(any(String.class), any(String.class)))
                .thenReturn(true);

        assertThrows(
                CredentialsAlreadyInUseException.class,
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