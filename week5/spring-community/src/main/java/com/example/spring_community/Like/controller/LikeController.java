package com.example.spring_community.Like.controller;

import com.example.spring_community.Auth.dto.AuthUserDto;
import com.example.spring_community.Exception.CustomException;
import com.example.spring_community.Exception.ErrorCode;
import com.example.spring_community.Exception.dto.DataResponseDto;
import com.example.spring_community.Like.dto.LikeDto;
import com.example.spring_community.Like.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name="Like API", description = "Like 리소스에 관한 API 입니다.")
@RequestMapping("/posts/{postId}/likes")
public class LikeController {
    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @GetMapping
    @Operation(summary = "좋아요 여부 조회", description = "postId에 해당하는 게시글에 대한 사용자의 좋아요 여부를 조회합니다.")
    public ResponseEntity<DataResponseDto<Boolean>> isLiked(HttpServletRequest request, @PathVariable Long postId) {
        AuthUserDto authUser = (AuthUserDto) request.getAttribute("authUser");
        if (authUser == null) throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        Boolean isLiked = likeService.isLiked(postId, authUser.getUserId());
        return ResponseEntity.status(HttpStatus.OK)
                .body(DataResponseDto.of(HttpStatus.OK, "GET_LIKES_SUCCESS", "좋아요 여부를 성공적으로 조회했습니다.", isLiked));
    }

    @PostMapping
    @Operation(summary = "좋아요 생성", description = "postId에 해당하는 게시글에 대한 사용자의 좋아요를 생성합니다.")
    public ResponseEntity<DataResponseDto<LikeDto>> addLikes(HttpServletRequest request, @PathVariable Long postId) {
        AuthUserDto authUser = (AuthUserDto) request.getAttribute("authUser");
        LikeDto likeDto = likeService.addLikes(postId, authUser.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(DataResponseDto.of(HttpStatus.CREATED, "POST_LIKES_SUCCESS", "좋아요를 성공적으로 눌렀습니다.", likeDto));
    }

    @DeleteMapping
    @Operation(summary = "좋아요 삭제", description = "postId에 해당하는 게시글에 대한 사용자의 좋아요 여부를 확인한 후 존재한다면 해당 좋아요를 삭제합니다.")
    public ResponseEntity<DataResponseDto<LikeDto>> deleteLikes(HttpServletRequest request, @PathVariable Long postId) {
        AuthUserDto authUser = (AuthUserDto) request.getAttribute("authUser");
        LikeDto likeDto = likeService.deleteLikes(postId, authUser.getUserId());
        return ResponseEntity.status(HttpStatus.OK)
                .body(DataResponseDto.of(HttpStatus.OK, "DELETE_LIKES_SUCCESS", "좋아요를 성공적으로 취소했습니다.", likeDto));
    }

}
