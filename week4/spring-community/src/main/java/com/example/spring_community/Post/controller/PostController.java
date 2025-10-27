package com.example.spring_community.Post.controller;

import com.example.spring_community.Auth.dto.AuthUserDto;
import com.example.spring_community.Exception.CustomException;
import com.example.spring_community.Exception.ErrorCode;
import com.example.spring_community.Exception.dto.DataResponseDto;
import com.example.spring_community.Exception.dto.ResponseDto;
import com.example.spring_community.Post.dto.*;
import com.example.spring_community.Post.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    //게시글 목록 조회
    @GetMapping
    public ResponseEntity<DataResponseDto<PagePostDto<PostDto>>> loadPostList(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        if (page < 0 || size < 1 || size > 50) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        PagePostDto<PostDto> postItems = postService.readPostPage(page, size);
        return ResponseEntity.status(HttpStatus.OK)
                .body(DataResponseDto.of(HttpStatus.OK, "READ_POSTPAGE_SUCCESS", "게시글 목록 조회에 성공했습니다.", postItems));
    }

    //게시글 상세 조회
    @GetMapping("/{postId}")
    public ResponseEntity<DataResponseDto<DetailPostDto>> loadPost(@PathVariable Long postId) {
        DetailPostDto post = postService.readPost(postId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(DataResponseDto.of(HttpStatus.OK, "READ_POST_SUCCESS", "게시글 조회에 성공했습니다.", post));
    }

    //게시글 생성
    @PostMapping
    public ResponseEntity<DataResponseDto<PostDto>> createPost(HttpServletRequest request, @RequestBody NewPostDto newPostDto) {
        AuthUserDto authUser = (AuthUserDto) request.getAttribute("authUser");
        if (authUser == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }
        PostDto newPost = postService.createPost(authUser.getUserId(), newPostDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(DataResponseDto.of(HttpStatus.CREATED, "CREATE_POST_SUCCESS", "성공적으로 게시글을 업로드했습니다.", newPost));
    }

    //게시글 수정
    @PatchMapping("/{postId}")
    public ResponseEntity<DataResponseDto<NewPostDto>> updatePost(HttpServletRequest request, @PathVariable long postId, @RequestBody UpdatePostDto updatePostDto) {
        AuthUserDto authUser = (AuthUserDto) request.getAttribute("authUser");
        if (authUser == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }
        NewPostDto updatedPost = postService.updatePost(authUser.getUserId(), postId, updatePostDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(DataResponseDto.of(HttpStatus.OK, "UPDATE_POST_SUCCESS", "성공적으로 게시글을 수정했습니다.", updatedPost));
    }

    //게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<ResponseDto> deletePost(HttpServletRequest request, @PathVariable long postId) {
        AuthUserDto authUser = (AuthUserDto) request.getAttribute("authUser");
        if (authUser == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }
        postService.deletePost(authUser.getUserId(), postId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.of(HttpStatus.OK, "DELETE_POST_SUCCESS", "성공적으로 게시글을 삭제했습니다."));
    }

}
