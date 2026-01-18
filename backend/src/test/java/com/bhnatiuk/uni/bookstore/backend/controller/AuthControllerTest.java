package com.bhnatiuk.uni.bookstore.backend.controller;

import com.bhnatiuk.uni.bookstore.backend.config.security.JwtAuthenticationFilter;
import com.bhnatiuk.uni.bookstore.backend.model.dto.TokenResponse;
import com.bhnatiuk.uni.bookstore.backend.model.dto.UserLoginRequest;
import com.bhnatiuk.uni.bookstore.backend.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = AuthController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {JwtAuthenticationFilter.class}
        )
)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {
    public static final String LOGIN_PATH = "/api/auth/login";
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void register() {
    }

    @Test
    void login_shouldReturnOkJwtResponse_whenLoginSuccessful() throws Exception {
        // given
        UserLoginRequest loginRequest =
                new UserLoginRequest("testUser", "testPassword");

        TokenResponse expectedResponse =
                new TokenResponse("test.jwt.token", "Bearer ", 1000 * 60);

        when(authService.login(any(UserLoginRequest.class))).thenReturn(expectedResponse);

        // when + then
        mockMvc.perform(post(LOGIN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwtToken").value(expectedResponse.jwtToken()))
                .andExpect(jsonPath("$.tokenType").value(expectedResponse.tokenType()))
                .andExpect(jsonPath("$.expiresIn").value(expectedResponse.expiresIn()));
    }

    @Test
    void login_shouldReturnUnauthorized_whenLoginFailed() {}
}