package com.bhnatiuk.uni.bookstore.backend.config.exception;

import com.bhnatiuk.uni.bookstore.backend.config.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = GlobalExceptionHandlerTest.TestController.class,
        excludeFilters = @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = {JwtAuthenticationFilter.class}
        )
)
@AutoConfigureMockMvc(addFilters = false)
@Import({ GlobalExceptionHandler.class, GlobalExceptionHandlerTest.TestController.class })
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExceptionMapper<HttpStatus> exceptionMapper;

    @RestController
    @RequestMapping("/test")
    static class TestController {
        @GetMapping("/exception")
        void testException() {
            throw new RuntimeException("Test Exception");
        }

        @GetMapping("/noException")
        ResponseEntity<String> noException() {
            return ResponseEntity.ok("No Exception");
        }
    }

    @BeforeEach
    void setUp() {
        when(exceptionMapper.map(any(Exception.class)))
                .thenReturn(HttpStatus.BAD_REQUEST);
    }

    @Test
    void handleGlobalException_shouldMapToCorrectStatus_whenExceptionThrown() throws Exception {
        mockMvc.perform(get("/test/exception"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Test Exception"))
                .andExpect(jsonPath("$.path").value("/test/exception"));
    }

    @Test
    void handleGlobalException_shouldReturnResponse_whenExceptionNotThrown() throws Exception {
        mockMvc.perform(get("/test/noException"))
                .andExpect(status().isOk())
                .andExpect(content().string("No Exception"));
    }
}