package com.example.spring_community.Post.dto;

import com.example.spring_community.Post.domain.PostEntity;
import com.example.spring_community.Post.domain.PostMetaEntity;
import com.example.spring_community.User.domain.Author;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
public class DetailPostDto {
    private final Long postId;
    private final String title;
    private final String content;
    private final String postImg;
    private final Author author;
    private final Integer likes;
    private final Integer views;
    private final Integer comments;
    private final Instant createdAt;
    private final Instant updatedAt;

    @Builder
    public DetailPostDto(Long postId, String title, String content, String postImg, Author author, Integer likes, Integer views, Integer comments,
                   Instant createdAt, Instant updatedAt){
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.postImg = postImg;
        this.author = author;
        this.likes = likes;
        this.views = views;
        this.comments = comments;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static DetailPostDto of (PostEntity post, PostMetaEntity postMeta) {
        Author author = new Author(post.getUser().getUserId(), post.getUser().getNickname());
        return new DetailPostDto(post.getPostId(), post.getTitle(), post.getContent(), post.getPostImg(), author,
                postMeta.getLikeCount(), postMeta.getViewCount(), postMeta.getCommentCount(), post.getCreatedAt(), post.getUpdatedAt());
    }
}
