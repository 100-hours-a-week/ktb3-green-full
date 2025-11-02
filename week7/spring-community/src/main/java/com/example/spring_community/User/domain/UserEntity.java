package com.example.spring_community.User.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long userId;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "image_url", nullable = false)
    private String profileImg;

    @Column(name = "active", nullable = false)
    private Boolean active;

    protected UserEntity() {}

    @Builder(toBuilder = true)
    public UserEntity(Long userId, String email, String password, String nickname, String profileImg, Boolean active) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImg = profileImg;
        this.active = active;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
