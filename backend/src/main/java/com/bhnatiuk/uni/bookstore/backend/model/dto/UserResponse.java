package com.bhnatiuk.uni.bookstore.backend.model.dto;

import com.bhnatiuk.uni.bookstore.backend.model.entity.AppUser;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.util.HtmlUtils;

public record UserResponse(
        @NotBlank
        Long id,

        @NotBlank
        String username,

        @NotBlank
        String email
) {
    public static UserResponse from(AppUser user) {
        return new UserResponse(
                user.getId(),
                HtmlUtils.htmlEscape(user.getUsername()),
                HtmlUtils.htmlEscape(user.getEmail())
        );
    }
}
