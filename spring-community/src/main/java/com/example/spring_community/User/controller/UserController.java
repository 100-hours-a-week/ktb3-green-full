package com.example.spring_community.User.controller;

import com.example.spring_community.Auth.dto.AuthUserDto;
import com.example.spring_community.Exception.dto.DataResponseDto;
import com.example.spring_community.Exception.dto.ResponseDto;
import com.example.spring_community.User.dto.CreateUserDto;
import com.example.spring_community.User.dto.UpdateUserProfileDto;
import com.example.spring_community.User.dto.UpdateUserPwDto;
import com.example.spring_community.Exception.CustomException;
import com.example.spring_community.Exception.ErrorCode;
import com.example.spring_community.User.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //회원가입
    @PostMapping("/new")
    public ResponseEntity<DataResponseDto<CreateUserDto>> signup(@RequestBody CreateUserDto createUserDto) {
        CreateUserDto createdUser = userService.signup(createUserDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(DataResponseDto.of(HttpStatus.CREATED, "CREATED", "회원가입에 성공했습니다", createdUser));
    }

    //회원탈퇴
    @PatchMapping("/active")
    public ResponseEntity<ResponseDto> withdraw(HttpServletRequest request) {
        AuthUserDto authUser = (AuthUserDto) request.getAttribute("authUser");
        if (authUser == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }
        userService.withdrawUser(authUser.getUserId());
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.of(HttpStatus.OK, "WITHDRAW_USER_SUCCESS", "정상적으로 탈퇴 처리 되었습니다."));
    }

    //회원 프로필 수정
    @PatchMapping("/profile")
    public ResponseEntity<DataResponseDto<CreateUserDto>> updateUserProfile(HttpServletRequest request, @RequestBody UpdateUserProfileDto updateUserProfileDto) {
        AuthUserDto authUser = (AuthUserDto) request.getAttribute("authUser");
        if (authUser == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }
        CreateUserDto updatedUser = userService.updateUserProfile(authUser.getUserId(), updateUserProfileDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(DataResponseDto.of(HttpStatus.OK, "UPDATE_PROFILE_SUCCESS", "프로필을 성공적으로 수정했습니다.", updatedUser));
    }

    //회원 비밀번호 수정
    @PatchMapping("/password")
    public ResponseEntity<Void> updateUserPw(HttpServletRequest request, @RequestBody UpdateUserPwDto updateUserPwDto) {
        AuthUserDto authUser = (AuthUserDto) request.getAttribute("authUser");
        if (authUser == null) throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        userService.updateUserPw(authUser.getUserId(), updateUserPwDto);
        return ResponseEntity.noContent().build();
    }
}
