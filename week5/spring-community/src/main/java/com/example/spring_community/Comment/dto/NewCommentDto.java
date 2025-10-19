package com.example.spring_community.Comment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class NewCommentDto {
    String content;

    @Builder(toBuilder = true)
    public NewCommentDto(String content) {
        this.content = content;
    }
}
