package com.example.spring_community.Post.dto;

import com.example.spring_community.Post.domain.PostEntity;
import org.springframework.lang.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
@Getter
public class NewPostDto {

    @NotBlank(message="제목을 입력해주세요.")
    @Size(max = 26, message = "제목은 최대 26자까지 작성 가능합니다.")
    private final String title;

    @NotBlank(message="본문을 입력해주세요.")
    private final String content;

    @Nullable
    private final String postImg;

    @Builder
    public NewPostDto(String title, String content, String postImg) {
        this.title = title;
        this.content = content;
        this.postImg = postImg;
    }

    public static NewPostDto of (PostEntity post) {
        return new NewPostDto(post.getTitle(), post.getContent(), post.getPostImg());
    }
}
