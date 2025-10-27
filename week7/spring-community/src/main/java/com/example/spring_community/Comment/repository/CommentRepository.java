package com.example.spring_community.Comment.repository;

import com.example.spring_community.Comment.domain.CommentEntity;
import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    public List<CommentEntity> findAllComments(Long postId);
    public Optional<CommentEntity> findById(Long commentId);
    public CommentEntity createComment(CommentEntity newCommentEntity);
    public CommentEntity updateComment(CommentEntity updateCommentEntity);
    public void deleteByCommentId(Long commentId);
}
