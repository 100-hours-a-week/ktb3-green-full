package com.example.spring_community.User.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateUserDto {
    private String email;
    private String password;
    private String nickname;
    private String profileImg;

    public CreateUserDto() {}

    public CreateUserDto(String email, String password, String nickname, String profileImg) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImg = profileImg;
    }
}
