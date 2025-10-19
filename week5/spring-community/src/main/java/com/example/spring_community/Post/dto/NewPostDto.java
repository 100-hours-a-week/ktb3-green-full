package com.example.spring_community.Post.dto;

import lombok.Builder;
import lombok.Getter;
@Getter
public class NewPostDto {

    private final String title;
    private final String content;
    private final String postImg;

    @Builder
    public NewPostDto(String title, String content, String postImg) {
        this.title = title;
        this.content = content;
        this.postImg = postImg;
    }
}
