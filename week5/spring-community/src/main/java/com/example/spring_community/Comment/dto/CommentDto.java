package com.example.spring_community.Comment.dto;

import com.example.spring_community.User.domain.Author;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
public class CommentDto {

    @Schema(description = "comment author", example = "{ userId: 9, }")
    private final Author author;

    @Schema(description = "email", example = "test@gmail.com")
    private final String content;

    @Schema(description = "email", example = "test@gmail.com")
    private final Instant updatedAt;

    @Builder(toBuilder = true)
    public CommentDto(Author author, String content, Instant updatedAt) {
        this.author = author;
        this.content = content;
        this.updatedAt = updatedAt;
    }
}

