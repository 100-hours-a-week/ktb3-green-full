package com.example.spring_community.Auth.dto;

import lombok.Builder;

@Builder
public record TokenDto (String accessToken, String refreshToken){
}
