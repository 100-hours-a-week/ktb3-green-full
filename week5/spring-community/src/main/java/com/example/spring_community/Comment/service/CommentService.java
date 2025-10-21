package com.example.spring_community.Comment.service;

import com.example.spring_community.Comment.dto.CommentDto;
import com.example.spring_community.Comment.dto.NewCommentDto;
import com.example.spring_community.Post.domain.PostEntity;
import com.example.spring_community.Post.repository.PostJsonRepository;
import com.example.spring_community.User.domain.Author;
import com.example.spring_community.Comment.domain.CommentEntity;
import com.example.spring_community.User.domain.UserEntity;
import com.example.spring_community.Exception.CustomException;
import com.example.spring_community.Exception.ErrorCode;
import com.example.spring_community.Comment.repository.CommentJsonRepository;
import com.example.spring_community.User.repository.UserJsonRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class CommentService {
    private final CommentJsonRepository commentRepository;
    private final UserJsonRepository userRepository;
    private final PostJsonRepository postRepository;

    public CommentService(CommentJsonRepository commentRepository, UserJsonRepository userRepository, PostJsonRepository postRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public void isValidPost (Long postId) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }

    public List<CommentDto> readCommentList(Long postId) {
        List<CommentEntity> commentEntityList = commentRepository.findAllComments(postId);
        return commentEntityList.stream()
                .map(comment -> CommentDto.builder()
                        .content(comment.getContent())
                        .author(comment.getAuthor())
                        .updatedAt(comment.getUpdatedAt())
                        .build()
                ).toList();
    }

    public CommentDto createComment(Long userId, Long postId, NewCommentDto newCommentDto) {
        UserEntity commentUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Instant now = Instant.now();
        Author commentAuthor = new Author(userId, commentUser.getNickname());
        CommentEntity newCommentEntity = CommentEntity.builder()
                        .postId(postId).author(commentAuthor)
                        .content(newCommentDto.getContent()).createdAt(now).updatedAt(now).build();

        CommentEntity createdComment = commentRepository.createComment(newCommentEntity);
        CommentDto createdCommentDto = CommentDto.builder()
                .author(createdComment.getAuthor())
                .content(createdComment.getContent())
                .updatedAt(createdComment.getUpdatedAt())
                .build();
        return createdCommentDto;
    }

    public CommentDto updateComment(Long userId, Long commentId, NewCommentDto newCommentDto) {
        CommentEntity commentEntity = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        UserEntity commentUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (!userId.equals(commentEntity.getAuthor().getUserId())) {
            throw new CustomException(ErrorCode.COMMENT_UPDATE_FORBIDDEN_USER);
        }
        Instant now = Instant.now();

        CommentEntity newCommentEntity = commentEntity.toBuilder()
                .content(newCommentDto.getContent()).updatedAt(now).build();
        CommentEntity updatedComment = commentRepository.updateComment(newCommentEntity);

        CommentDto updatedCommentDto = CommentDto.builder()
                .author(updatedComment.getAuthor())
                .content(updatedComment.getContent())
                .updatedAt(updatedComment.getUpdatedAt())
                .build();
        return updatedCommentDto;
    }

    public void deleteComment(Long userId, Long commentId) {
        UserEntity deleteUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        CommentEntity commentEntity = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        if (!userId.equals(commentEntity.getAuthor().getUserId())) {
            throw new CustomException(ErrorCode.COMMENT_DELETE_FORBIDDEN_USER);
        }
        commentRepository.deleteByCommentId(commentId);
    }
}
