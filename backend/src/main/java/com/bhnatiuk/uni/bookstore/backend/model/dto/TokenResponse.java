package com.bhnatiuk.uni.bookstore.backend.model.dto;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.util.HtmlUtils;

public record TokenResponse(
        @NotBlank String jwtToken,
        @NotBlank String tokenType,
        long expiresIn
) {
    public TokenResponse(String jwtToken, String tokenType, long expiresIn) {
        this.jwtToken = HtmlUtils.htmlEscape(jwtToken);
        this.tokenType = HtmlUtils.htmlEscape(tokenType);
        this.expiresIn = expiresIn;
    }
}
