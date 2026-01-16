package com.bhnatiuk.uni.bookstore.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenServiceTest {

    private JwtTokenService jwtTokenService;

    @BeforeEach
    void setUp() {
        jwtTokenService = new JwtTokenService(
                "SoMe_VeRy_Secure_Token_For_Tesing_purposes_0987",
                1000 * 60
        );
    }

    @Test
    void generateToken_shouldReturnCorrectUsername_whenPassedValidToken() {
        String token = jwtTokenService.generateToken("admin");
        assertEquals("admin", jwtTokenService.getUsername(token));
    }

    @Test
    void isValid_shouldReturnTrue_whenTokenIsValid() {
        String token = jwtTokenService.generateToken("admin");
        assertTrue(jwtTokenService.isValid(token));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @MethodSource("invalidTokensProvider")
    void isValid_shouldReturnFalse_whenTokenIsInvalid(String token) {
        assertFalse(jwtTokenService.isValid(token));
    }

    public static Stream<Arguments> invalidTokensProvider() {
        return Stream.of(
                Arguments.of("Invalid.Test.Token"),
                Arguments.of("  ")
        );
    }

    @Test
    void resolveToken_shouldReturnToken_whenHeaderIsValid() {
        String token = jwtTokenService.generateToken("admin");
        String header = "Bearer " + token;
        assertEquals(token, jwtTokenService.resolveToken(header));
    }

    @ParameterizedTest
    @MethodSource("invalidHeadersProvider")
    @NullAndEmptySource
    void resolveToken_shouldReturnNull_whenHeaderIsInvalid(String header) {
        assertNull(jwtTokenService.resolveToken(header));
    }

    public static Stream<Arguments> invalidHeadersProvider() {
        return Stream.of(
                Arguments.of(" "),
                Arguments.of("   "),
                Arguments.of("Bearer"),
                Arguments.of("Token")
        );
    }
}