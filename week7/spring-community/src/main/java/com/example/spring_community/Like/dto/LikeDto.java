package com.example.spring_community.Like.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LikeDto {

    private final Long postId;
    private final String title;
    private final int likes;

    @Builder(toBuilder = true)
    public LikeDto(Long postId, String title, int likes) {
        this.postId = postId;
        this.title = title;
        this.likes = likes;
    }
}
