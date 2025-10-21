package com.example.spring_community.Auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AuthUserDto {

    @Schema(description = "userId", example = "9")
    private final Long userId;

    @Builder
    public AuthUserDto(Long userId) {
        this.userId = userId;
    }
}
