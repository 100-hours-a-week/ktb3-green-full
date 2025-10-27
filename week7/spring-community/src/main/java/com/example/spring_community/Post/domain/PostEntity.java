package com.example.spring_community.Post.domain;

import com.example.spring_community.User.domain.Author;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;
import java.util.Comparator;

@Getter
@Setter
public class PostEntity implements Comparable<PostEntity> {
    private Long postId;
    private String title;
    private String content;
    private String postImg;
    private Author author;
    private Integer likes;
    private Integer views;
    private Integer comments;
    private Instant createdAt;
    private Instant updatedAt;

    @Builder(toBuilder = true)
    public PostEntity(Long postId, String title, String content, String postImg, Author author,
                      Integer likes, Integer views, Integer comments, Instant createdAt, Instant updatedAt) {
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

    @Override
    public int compareTo(PostEntity post) {
        return Comparator.comparing(PostEntity::getCreatedAt,
                        Comparator.nullsLast(Instant::compareTo))
                .thenComparing(PostEntity::getPostId,
                        Comparator.nullsLast(Long::compareTo)).reversed().compare(this, post);

    }
}