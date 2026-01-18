package com.bhnatiuk.uni.bookstore.backend.config.security;

import com.bhnatiuk.uni.bookstore.backend.entity.AppUser;
import com.bhnatiuk.uni.bookstore.backend.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {
    @Mock
    private TokenService tokenService;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private MockHttpServletRequest request;
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;
    private String jwt;
    private String header;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();

        jwt = "some.jwt.token";
        header = "Bearer " + jwt;
        request.addHeader("Authorization", header);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_shouldSetAuthentication_whenJwtIsValid() throws ServletException, IOException {
        // given
        AppUser expectedUser = new AppUser();
        expectedUser.setId(1L);
        expectedUser.setUsername("testUsername");
        expectedUser.setEmail("test@mail.com");
        expectedUser.setPassword("testPassword");

        when(tokenService.resolveToken(header)).thenReturn(jwt);
        when(tokenService.isValid(jwt)).thenReturn(true);
        when(tokenService.getUsername(jwt)).thenReturn(expectedUser.getUsername());

        when(userDetailsService.loadUserByUsername(expectedUser.getUsername()))
                .thenReturn(expectedUser);

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(filterChain).doFilter(request, response);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertTrue(authentication.isAuthenticated());
        assertEquals(expectedUser, authentication.getPrincipal());
    }

    @Test
    void doFilterInternal_shouldLeaveNullAuthentication_whenJwtIsInvalid() throws ServletException, IOException {
        // given
        when(tokenService.resolveToken(header)).thenReturn(jwt);
        when(tokenService.isValid(jwt)).thenReturn(false);

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(filterChain).doFilter(request, response);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);
    }

    @Test
    void doFilterInternal_shouldLeaveNullAuthentication_whenJwtIsNull() throws ServletException, IOException {
        // given
        when(tokenService.resolveToken(header)).thenReturn(null);

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(filterChain).doFilter(request, response);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);
    }

    @Test
    void doFilterInternal_shouldLeaveNullAuthentication_whenUserIsNotFound() throws ServletException, IOException {
        // given
        when(tokenService.resolveToken(header)).thenReturn(jwt);
        when(tokenService.getUsername(jwt)).thenReturn("unknown");
        when(tokenService.isValid(jwt)).thenReturn(true);
        when(userDetailsService.loadUserByUsername("unknown")).thenThrow(UsernameNotFoundException.class);

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(userDetailsService).loadUserByUsername("unknown");
        verify(filterChain).doFilter(request, response);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);
    }
}