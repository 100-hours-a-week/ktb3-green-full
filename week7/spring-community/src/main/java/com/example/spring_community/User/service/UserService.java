package com.example.spring_community.User.service;

import com.example.spring_community.User.dto.UpdateUserProfileDto;
import com.example.spring_community.User.dto.UpdateUserPwDto;
import com.example.spring_community.User.dto.CreateUserDto;
import com.example.spring_community.User.domain.UserEntity;
import com.example.spring_community.Exception.CustomException;
import com.example.spring_community.Exception.ErrorCode;
import com.example.spring_community.User.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
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

        userEntity.setNickname(newNickname);
        userEntity.setProfileImg(newProfileImg);

        return CreateUserDto.of(userEntity);
    }

    @Transactional
    public void updateUserPw(Long userId, UpdateUserPwDto updateUserPwDto) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (userEntity.getPassword().equals(updateUserPwDto.getPassword())) {
            throw new CustomException(ErrorCode.DUPLICATED_PASSWORD);
        }

        userEntity.setPassword(userEntity.getPassword());
    }

    @Transactional
    public CreateUserDto signup(CreateUserDto createUserDto) {
        if (!createUserDto.getPassword().equals(createUserDto.getCheckPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        if (userRepository.existsByEmail(createUserDto.getEmail())) {
            throw new CustomException(ErrorCode.DUPLICATED_USER);
        }

        String encodedPassword = passwordEncoder.encode(createUserDto.getPassword());
        UserEntity userEntity = UserEntity.builder()
                .email(createUserDto.getEmail())
                .password(encodedPassword)
                .nickname(createUserDto.getNickname())
                .profileImg(createUserDto.getProfileImg())
                .active(true)
                .build();

        userRepository.save(userEntity);

        return CreateUserDto.of(userEntity);
    }

    @Transactional
    public void withdrawUser(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!userEntity.getActive()) {
            throw new CustomException(ErrorCode.DUPLICATED_WITHDRAW);
        }

        userEntity.setActive(false);
    }

}
