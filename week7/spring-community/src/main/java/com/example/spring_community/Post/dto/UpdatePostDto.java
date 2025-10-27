package com.example.spring_community.Post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdatePostDto {
    private String title;
    private String content;
    private String postImg;
}
