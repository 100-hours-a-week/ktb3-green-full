package com.example.spring_community.Like.dto;

import com.example.spring_community.Post.domain.PostEntity;
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

    public static LikeDto of(PostEntity postEntity) {
        return LikeDto.builder()
                .postId(postEntity.getPostId())
                .title(postEntity.getTitle())
                .likes(postEntity.getPostMetaEntity().getLikeCount())
                .build();
    }
}
