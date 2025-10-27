package com.example.spring_community.Comment.controller;

import com.example.spring_community.Auth.dto.AuthUserDto;
import com.example.spring_community.Comment.dto.CommentDto;
import com.example.spring_community.Comment.dto.NewCommentDto;
import com.example.spring_community.Exception.dto.DataResponseDto;
import com.example.spring_community.Exception.dto.ResponseDto;
import com.example.spring_community.Exception.CustomException;
import com.example.spring_community.Exception.ErrorCode;
import com.example.spring_community.Comment.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    //댓글 목록 조회
    @GetMapping
    public ResponseEntity<DataResponseDto<List<CommentDto>>> loadCommentList(@PathVariable Long postId) {
        commentService.isValidPost(postId);
        List<CommentDto> commentList = commentService.readCommentList(postId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(DataResponseDto.of(HttpStatus.OK, "READ_COMMNETLIST_SUCCESS", "댓글 목록 조회에 성공했습니다.", commentList));
    }

    //댓글 생성
    @PostMapping
    public ResponseEntity<DataResponseDto<CommentDto>> createComment(HttpServletRequest request, @PathVariable Long postId, @RequestBody NewCommentDto newCommentDto) {
        commentService.isValidPost(postId);
        AuthUserDto authUser = (AuthUserDto) request.getAttribute("authUser");
        if (authUser == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }
        CommentDto newComment = commentService.createComment(authUser.getUserId(), postId, newCommentDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(DataResponseDto.of(HttpStatus.CREATED, "CREATE_COMMENT_SUCCESS", "성공적으로 댓글을 업로드했습니다.", newComment));
    }

    //댓글 수정
    @PatchMapping("/{commentId}")
    public ResponseEntity<DataResponseDto<CommentDto>> updatePost(HttpServletRequest request, @PathVariable long postId,@PathVariable long commentId, @RequestBody NewCommentDto updateCommentDto) {
        commentService.isValidPost(postId);
        AuthUserDto authUser = (AuthUserDto) request.getAttribute("authUser");
        if (authUser == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }
        CommentDto updatedComment = commentService.updateComment(authUser.getUserId(), commentId, updateCommentDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(DataResponseDto.of(HttpStatus.OK, "UPDATE_COMMENT_SUCCESS", "성공적으로 댓글을 수정했습니다.", updatedComment));
    }

    //댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ResponseDto> deletePost(HttpServletRequest request, @PathVariable long postId, @PathVariable long commentId) {
        commentService.isValidPost(postId);
        AuthUserDto authUser = (AuthUserDto) request.getAttribute("authUser");
        if (authUser == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }
        commentService.deleteComment(authUser.getUserId(), commentId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.of(HttpStatus.OK, "DELETE_COMMENT_SUCCESS", "성공적으로 댓글을 삭제했습니다."));
    }
}
