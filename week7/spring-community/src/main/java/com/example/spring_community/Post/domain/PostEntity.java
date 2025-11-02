package com.example.spring_community.Post.domain;

import com.example.spring_community.User.domain.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Entity
@Table(name = "posts")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private UserEntity user;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "image_url")
    private String postImg;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Instant updatedAt;

    @OneToOne(mappedBy = "post")
    private PostMetaEntity postMetaEntity;


    protected PostEntity() {}

    @Builder(toBuilder = true)
    public PostEntity(Long postId, UserEntity user, String title, String content, String postImg, Instant createdAt, Instant updatedAt, PostMetaEntity postMetaEntity) {
        this.postId = postId;
        this.user = user;
        this.title = title;
        this.content = content;
        this.postImg = postImg;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.postMetaEntity = postMetaEntity;
    }

    public void initMetaIfAbsent() {
        if (this.postMetaEntity == null) {
            this.postMetaEntity = PostMetaEntity.of(this);
        }
    }

}
