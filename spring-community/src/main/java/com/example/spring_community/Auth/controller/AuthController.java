package com.example.spring_community.Auth.controller;

import com.example.spring_community.Auth.dto.AuthUserDto;
import com.example.spring_community.Auth.dto.LoginDto;
import com.example.spring_community.Auth.dto.RefreshTokenDto;
import com.example.spring_community.Auth.dto.TokenDto;
import com.example.spring_community.Exception.dto.DataResponseDto;
import com.example.spring_community.Exception.dto.ResponseDto;
import com.example.spring_community.Exception.CustomException;
import com.example.spring_community.Exception.ErrorCode;
import com.example.spring_community.Auth.jwt.JwtUtils;
import com.example.spring_community.Auth.service.AuthService;
import com.example.spring_community.Auth.service.BlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final BlacklistService blacklistService;
    private final JwtUtils jwtUtils;

    public AuthController(AuthService authService, BlacklistService blacklistService, JwtUtils jwtUtils) {
        this.authService = authService;
        this.blacklistService = blacklistService;
        this.jwtUtils = jwtUtils;
    }

    //로그인
    @PostMapping("/token")
    public ResponseEntity<DataResponseDto<TokenDto>> login(@RequestBody LoginDto loginDto) {
        TokenDto tokenDto = authService.login(loginDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(DataResponseDto.of(HttpStatus.OK, "LOGIN_SUCCESS", "로그인에 성공했습니다.", tokenDto));
    }

    //로그아웃
    @DeleteMapping("/token")
    public ResponseEntity<ResponseDto> logout(@RequestHeader(value="Authorization") String authorization) {
        String accessToken = (authorization != null && authorization.startsWith("Bearer")) ? authorization.substring(7) : null;
        if (accessToken == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }
        jwtUtils.verifyToken(accessToken);
        blacklistService.blacklist(accessToken, jwtUtils.getAccessExpiration());
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.of(HttpStatus.OK, "LOGOUT_SUCCESS", "정상적으로 로그아웃되었습니다."));
    }

    //토큰 재발급
    @PostMapping("/token/new")
    public ResponseEntity<DataResponseDto<TokenDto>> refresh(HttpServletRequest request, @RequestBody RefreshTokenDto refreshTokenDto) {
        AuthUserDto authUser = (AuthUserDto) request.getAttribute("authUser");
        if (authUser == null) throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        TokenDto refreshedToken = authService.reissueAccessToken(authUser.getUserId(), refreshTokenDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(DataResponseDto.of(HttpStatus.OK, "REFRESH_USER_SUCCESS", "정상적으로 토큰이 재발급되었습니다.", refreshedToken));
    }
}
