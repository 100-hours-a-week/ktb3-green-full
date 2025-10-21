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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.websocket.OnError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@Tag(name="User API", description = "User 리소스에 관한 API 입니다.")
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/new")
    @Operation(summary = "회원가입", description = "입력받은 사용자 정보로 회원 가입을 진행합니다.")
    public ResponseEntity<DataResponseDto<CreateUserDto>> signup(@RequestBody @Valid CreateUserDto createUserDto) {
        CreateUserDto createdUser = userService.signup(createUserDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(DataResponseDto.of(HttpStatus.CREATED, "CREATED", "회원가입에 성공했습니다", createdUser));
    }

    @PatchMapping("/active")
    @Operation(summary = "회원탈퇴", description = "soft delete를 통한 회원 탈퇴를 진행합니다.")
    public ResponseEntity<ResponseDto> withdraw(HttpServletRequest request) {
        AuthUserDto authUser = (AuthUserDto) request.getAttribute("authUser");
        if (authUser == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }
        userService.withdrawUser(authUser.getUserId());
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.of(HttpStatus.OK, "WITHDRAW_USER_SUCCESS", "정상적으로 탈퇴 처리 되었습니다."));
    }

    @PatchMapping("/profile")
    @Operation(summary = "사용자 프로필 수정", description = "닉네임과 프로필 이미지를 변경합니다.")
    public ResponseEntity<DataResponseDto<CreateUserDto>> updateUserProfile(HttpServletRequest request, @RequestBody UpdateUserProfileDto updateUserProfileDto) {
        AuthUserDto authUser = (AuthUserDto) request.getAttribute("authUser");
        if (authUser == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }
        CreateUserDto updatedUser = userService.updateUserProfile(authUser.getUserId(), updateUserProfileDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(DataResponseDto.of(HttpStatus.OK, "UPDATE_PROFILE_SUCCESS", "프로필을 성공적으로 수정했습니다.", updatedUser));
    }

    @PatchMapping("/password")
    @Operation(summary = "사용자 비밀번호 수정", description = "비밀번호를 수정합니다. 이전과 동일한 비밀번호로는 수정할 수 없습니다.")
    public ResponseEntity<Void> updateUserPw(HttpServletRequest request, @RequestBody UpdateUserPwDto updateUserPwDto) {
        AuthUserDto authUser = (AuthUserDto) request.getAttribute("authUser");
        if (authUser == null) throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        userService.updateUserPw(authUser.getUserId(), updateUserPwDto);
        return ResponseEntity.noContent().build();
    }
}
