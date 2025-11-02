package com.example.spring_community.Post.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "post_meta")
public class PostMetaEntity {

    @Id
    @Column(name = "post_id")
    private Long postId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @Column(name = "like_count", nullable = false)
    private int likeCount;

    @Column(name = "view_count", nullable = false)
    private int viewCount;

    @Column(name = "comment_count", nullable = false)
    private int commentCount;

    public PostMetaEntity() {};

    public static PostMetaEntity of (PostEntity post) {
        PostMetaEntity postMetaEntity = new PostMetaEntity();
        postMetaEntity.post = post;
        postMetaEntity.likeCount = 0;
        postMetaEntity.viewCount = 0;
        postMetaEntity.commentCount = 0;
        return postMetaEntity;
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        this.likeCount--;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

    public void decreaseViewCount() {
        this.viewCount--;
    }

    public void increaseCommentCount() {
        this.commentCount++;
    }

    public void decreaseCommentCount() {
        this.commentCount--;
    }

}
