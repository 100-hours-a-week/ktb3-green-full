package com.example.spring_community.User.domain;

import lombok.Getter;

@Getter
public class Author {
    private Long userId;
    private String nickname;

    public Author(Long userId, String nickname) {
        this.userId = userId;
        this.nickname = nickname;
    }
}
