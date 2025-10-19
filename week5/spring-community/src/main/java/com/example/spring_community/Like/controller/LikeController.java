package com.example.spring_community.Like.controller;

import com.example.spring_community.Auth.dto.AuthUserDto;
import com.example.spring_community.Exception.CustomException;
import com.example.spring_community.Exception.ErrorCode;
import com.example.spring_community.Exception.dto.DataResponseDto;
import com.example.spring_community.Exception.dto.ResponseDto;
import com.example.spring_community.Like.dto.LikeDto;
import com.example.spring_community.Post.domain.PostEntity;
import com.example.spring_community.Like.service.LikeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts/{postId}/likes")
public class LikeController {
    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    //좋아요 여부 조회
    @GetMapping
    public ResponseEntity<DataResponseDto<Boolean>> isLiked(HttpServletRequest request, @PathVariable Long postId) {
        AuthUserDto authUser = (AuthUserDto) request.getAttribute("authUser");
        if (authUser == null) throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        Boolean isLiked = likeService.isLiked(postId, authUser.getUserId());
        return ResponseEntity.status(HttpStatus.OK)
                .body(DataResponseDto.of(HttpStatus.OK, "GET_LIKES_SUCCESS", "좋아요 여부를 성공적으로 조회했습니다.", isLiked));
    }

    //좋아요 생성
    @PostMapping
    public ResponseEntity<DataResponseDto<LikeDto>> addLikes(HttpServletRequest request, @PathVariable Long postId) {
        AuthUserDto authUser = (AuthUserDto) request.getAttribute("authUser");
        PostEntity postEntity = likeService.addLikes(postId, authUser.getUserId());
        LikeDto likeDto = LikeDto.builder()
                .postId(postEntity.getPostId()).title(postEntity.getTitle())
                .likes(postEntity.getLikes())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(DataResponseDto.of(HttpStatus.CREATED, "POST_LIKES_SUCCESS", "좋아요를 성공적으로 눌렀습니다.", likeDto));
    }

    //좋아요 삭제
    @DeleteMapping
    public ResponseEntity<DataResponseDto<LikeDto>> deleteLikes(HttpServletRequest request, @PathVariable Long postId) {
        AuthUserDto authUser = (AuthUserDto) request.getAttribute("authUser");
        PostEntity postEntity = likeService.deleteLikes(postId, authUser.getUserId());
        LikeDto likeDto = LikeDto.builder()
                .postId(postEntity.getPostId()).title(postEntity.getTitle())
                .likes(postEntity.getLikes())
                .build();
        return ResponseEntity.status(HttpStatus.OK)
                .body(DataResponseDto.of(HttpStatus.OK, "DELETE_LIKES_SUCCESS", "좋아요를 성공적으로 취소했습니다.", likeDto));
    }

}
