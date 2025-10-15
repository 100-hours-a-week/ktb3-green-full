package com.example.spring_community.Auth.service;

import com.example.spring_community.Auth.dto.RefreshTokenDto;
import com.example.spring_community.Auth.dto.TokenDto;
import com.example.spring_community.User.domain.UserEntity;
import com.example.spring_community.Exception.CustomException;
import com.example.spring_community.Exception.ErrorCode;
import com.example.spring_community.Auth.jwt.JwtUtils;
import com.example.spring_community.Auth.dto.LoginDto;
import com.example.spring_community.User.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    public AuthService(UserRepository userRepository, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }
    public TokenDto login(LoginDto loginDto) {
        if (loginDto == null ||
                !StringUtils.hasText(loginDto.email()) ||
                !StringUtils.hasText(loginDto.password())) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        UserEntity userEntity = userRepository.findByEmail(loginDto.email())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if(!userRepository.isValidUser(loginDto)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }

        String accessToken = jwtUtils.createAccessToken(userEntity);
        String refreshToken = jwtUtils.createRefreshToken(userEntity);

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public TokenDto reissueAccessToken(Long userId, RefreshTokenDto refreshTokenDto) {
        if(!jwtUtils.verifyToken(refreshTokenDto.getRefreshToken())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_TOKEN);
        }
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        String newAccessToken = jwtUtils.createAccessToken(userEntity);
        TokenDto newTokenDto = TokenDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshTokenDto.getRefreshToken())
                .build();
        return newTokenDto;
    }
}
