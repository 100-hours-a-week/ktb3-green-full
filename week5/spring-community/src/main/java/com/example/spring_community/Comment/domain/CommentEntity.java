package com.example.spring_community.Comment.domain;

import com.example.spring_community.User.domain.Author;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class CommentEntity {
    private Long commentId;
    private Long postId;
    private Author author;
    private String content;
    private Instant createdAt;
    private Instant updatedAt;

    @Builder(toBuilder = true)
    public CommentEntity(Long commentId, Long postId, Author author, String content, Instant createdAt, Instant updatedAt) {
        this.commentId = commentId;
        this.postId = postId;
        this.author = author;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
