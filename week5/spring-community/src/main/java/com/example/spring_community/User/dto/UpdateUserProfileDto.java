package com.example.spring_community.User.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateUserProfileDto {
    private String nickname;
    private String profileImg;

    public UpdateUserProfileDto(String nickname, String profileImg) {
        this.nickname = nickname;
        this.profileImg = profileImg;
    }
}
