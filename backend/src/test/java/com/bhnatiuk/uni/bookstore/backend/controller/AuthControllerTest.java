package com.bhnatiuk.uni.bookstore.backend.controller;

import com.bhnatiuk.uni.bookstore.backend.config.exception.GlobalExceptionHandler;
import com.bhnatiuk.uni.bookstore.backend.config.exception.HttpStatusExceptionMapper;
import com.bhnatiuk.uni.bookstore.backend.config.security.JwtAuthenticationFilter;
import com.bhnatiuk.uni.bookstore.backend.model.dto.TokenResponse;
import com.bhnatiuk.uni.bookstore.backend.model.dto.UserLoginRequest;
import com.bhnatiuk.uni.bookstore.backend.model.dto.UserRegisterRequest;
import com.bhnatiuk.uni.bookstore.backend.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = AuthController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {JwtAuthenticationFilter.class}
        )
)
@Import({
        GlobalExceptionHandler.class,
        HttpStatusExceptionMapper.class
})
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {
    public static final String LOGIN_PATH = "/api/auth/login";
    public static final String REGISTER_PATH = "/api/auth/register";
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    private <T> void testInvalidDtoRequest(T request, String requestPath) throws Exception {
        mockMvc.perform(post(requestPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.path").value(requestPath));
    }

    @Test
    void register_shouldReturnOkResponse_whenValidRequest() throws Exception {
        UserRegisterRequest request =
                new UserRegisterRequest("testUsername", "example@mail.com", "password");

        TokenResponse expectedResponse =
                new TokenResponse("test.jwt.token", "Bearer ", 1000 * 60);

        when(authService.register(request)).thenReturn(expectedResponse);

        mockMvc.perform(post(REGISTER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwtToken").value(expectedResponse.jwtToken()))
                .andExpect(jsonPath("$.tokenType").value(expectedResponse.tokenType()))
                .andExpect(jsonPath("$.expiresIn").value(expectedResponse.expiresIn()));

        verify(authService).register(any(UserRegisterRequest.class));
    }

    static Stream<UserRegisterRequest> invalidRegisterRequestProvider() {
        return Stream.of(
                new UserRegisterRequest(null, "valid@mail.com", "validPassword"),
                new UserRegisterRequest("", "valid@mail.com", "validPassword"),
                new UserRegisterRequest(" ", "valid@mail.com", "validPassword"),
                new UserRegisterRequest("  ", "valid@mail.com", "validPassword"),

                new UserRegisterRequest("ValidUsername", null, "validPassword"),
                new UserRegisterRequest("ValidUsername", "", "validPassword"),
                new UserRegisterRequest("ValidUsername", " ", "validPassword"),
                new UserRegisterRequest("ValidUsername", "  ", "validPassword"),
                new UserRegisterRequest("ValidUsername", "whitespaces in username1@mail.com", "validPassword"),
                new UserRegisterRequest("ValidUsername", "whitespacesInUsername2 @mail.com", "validPassword"),
                new UserRegisterRequest("ValidUsername", "username@whitespace in domain1.com", "validPassword"),
                new UserRegisterRequest("ValidUsername", "username@mail.whitespace in domain2", "validPassword"),
                new UserRegisterRequest("ValidUsername", "username@mail. whitespaceInDomain3", "validPassword"),
                new UserRegisterRequest("ValidUsername", "missingDomain", "validPassword"),
                new UserRegisterRequest("ValidUsername", "@missingusername.com", "validPassword"),
                new UserRegisterRequest("ValidUsername", "missingDomain@.com", "validPassword"),
                new UserRegisterRequest("ValidUsername", "missingAtSign.com", "validPassword"),

                new UserRegisterRequest("ValidUsername", "valid@mail.com", null),
                new UserRegisterRequest("ValidUsername", "valid@mail.com", ""),
                new UserRegisterRequest("ValidUsername", "valid@mail.com", " "),
                new UserRegisterRequest("ValidUsername", "valid@mail.com", "  ")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidRegisterRequestProvider")
    void register_shouldReturnBadRequest_whenDtoIsNotValid(UserRegisterRequest registerRequest) throws Exception {
        testInvalidDtoRequest(registerRequest, REGISTER_PATH);
    }

    @Test
    void login_shouldReturnOkJwtResponse_whenValidRequest() throws Exception {
        UserLoginRequest loginRequest =
                new UserLoginRequest("testUser", "testPassword");

        TokenResponse expectedResponse =
                new TokenResponse("test.jwt.token", "Bearer ", 1000 * 60);

        when(authService.login(any(UserLoginRequest.class))).thenReturn(expectedResponse);

        mockMvc.perform(post(LOGIN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwtToken").value(expectedResponse.jwtToken()))
                .andExpect(jsonPath("$.tokenType").value(expectedResponse.tokenType()))
                .andExpect(jsonPath("$.expiresIn").value(expectedResponse.expiresIn()));
    }

    static Stream<UserLoginRequest> invalidLoginRequestProvider() {
        return Stream.of(
                new UserLoginRequest(null, "validPassword"),
                new UserLoginRequest("", "validPassword"),
                new UserLoginRequest(" ", "validPassword"),
                new UserLoginRequest("  ", "validPassword"),
                new UserLoginRequest("validUsername", null),
                new UserLoginRequest("validUsername", ""),
                new UserLoginRequest("validUsername", " "),
                new UserLoginRequest("validUsername", "  "),
                new UserLoginRequest(null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidLoginRequestProvider")
    void login_shouldReturnBadRequest_whenDtoValidationFails(UserLoginRequest request) throws Exception {
        testInvalidDtoRequest(request, LOGIN_PATH);
    }

    @Test
    void login_shouldReturnUnauthorized_whenLoginToUnexistentUser() throws Exception {
        UserLoginRequest loginRequest =
                new UserLoginRequest("testUser", "testPassword");

        when(authService.login(any(UserLoginRequest.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        mockMvc.perform(post(LOGIN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }
}