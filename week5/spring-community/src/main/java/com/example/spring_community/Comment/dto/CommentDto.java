package com.example.spring_community.Comment.dto;

import com.example.spring_community.User.domain.Author;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
public class CommentDto {

    private final Author author;
    private final String content;
    private final Instant updatedAt;

    @Builder(toBuilder = true)
    public CommentDto(Author author, String content, Instant updatedAt) {
        this.author = author;
        this.content = content;
        this.updatedAt = updatedAt;
    }
}

