package com.example.spring_community.User.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEntity {
    private Long userId;
    private String email;
    private String password;
    private String nickname;
    private String profileImg;
    private Boolean active;

    @Builder(toBuilder = true)
    public UserEntity(Long userId, String email, String password, String nickname, String profileImg, Boolean active) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImg = profileImg;
        this.active = active;
    }
}