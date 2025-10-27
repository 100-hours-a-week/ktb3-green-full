package com.example.spring_community.Post.dto;

import com.example.spring_community.User.domain.Author;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
public class PostDto {
    private final Long postId;
    private final String title;
    private final Author author;
    private final Integer likes;
    private final Integer views;
    private final Integer comments;
    private final Instant createdAt;
    private final Instant updatedAt;

    @Builder
    public PostDto(Long postId, String title, Author author, Integer likes, Integer views, Integer comments,
                   Instant createdAt, Instant updatedAt){
        this.postId = postId;
        this.title = title;
        this.author = author;
        this.likes = likes;
        this.views = views;
        this.comments = comments;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
