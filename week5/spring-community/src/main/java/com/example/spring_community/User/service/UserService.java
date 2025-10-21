package com.example.spring_community.User.service;

import com.example.spring_community.User.dto.UpdateUserProfileDto;
import com.example.spring_community.User.dto.UpdateUserPwDto;
import com.example.spring_community.User.dto.CreateUserDto;
import com.example.spring_community.User.domain.UserEntity;
import com.example.spring_community.Exception.CustomException;
import com.example.spring_community.Exception.ErrorCode;
import com.example.spring_community.User.repository.UserJsonRepository;
import com.example.spring_community.User.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public CreateUserDto updateUserProfile(Long userId, UpdateUserProfileDto updateUserProfileDto) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String newNickname = Optional.ofNullable(updateUserProfileDto.getNickname())
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .orElse(userEntity.getNickname());

        String newProfileImg = Optional.ofNullable(updateUserProfileDto.getProfileImg())
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .orElse(userEntity.getProfileImg());

        UserEntity updatedUser = userRepository.updateUserInfo(userEntity.toBuilder()
                .nickname(newNickname).profileImg(newProfileImg).build());

        CreateUserDto updatedUserDto = CreateUserDto.builder()
                .email(updatedUser.getEmail())
                .password(null)
                .checkPassword(null)
                .nickname(updatedUser.getNickname())
                .profileImg(updatedUser.getProfileImg()).build();

        return updatedUserDto;
    }

    public void updateUserPw(Long userId, UpdateUserPwDto updateUserPwDto) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (userEntity.getPassword().equals(updateUserPwDto.getPassword())) {
            throw new CustomException(ErrorCode.DUPLICATED_PASSWORD);
        }
        userRepository.updateUserInfo(userEntity.toBuilder()
                .password(updateUserPwDto.getPassword()).build());
    }

    public CreateUserDto signup(CreateUserDto createUserDto) {
        if (!createUserDto.getPassword().equals(createUserDto.getCheckPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        if (userRepository.findByEmail(createUserDto.getEmail()).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATED_USER);
        }
        UserEntity userEntity = UserEntity.builder()
                .email(createUserDto.getEmail())
                .password(createUserDto.getPassword())
                .nickname(createUserDto.getNickname())
                .profileImg(createUserDto.getProfileImg())
                .active(true)
                .build();
        UserEntity createdUser = userRepository.createNewUser(userEntity);
        CreateUserDto createdUserDto = CreateUserDto.builder()
                .email(createdUser.getEmail())
                .password(null)
                .checkPassword(null)
                .nickname(createdUser.getNickname())
                .profileImg(createdUser.getProfileImg()).build();
        return createdUserDto;
    }

    public void withdrawUser(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (!userEntity.getActive()) {
            throw new CustomException(ErrorCode.DUPLICATED_WITHDRAW);
        }
        UserEntity updateUserActiveEntity = userEntity.toBuilder().active(false).build();
        userRepository.updateUserInfo(updateUserActiveEntity);
    }

}
