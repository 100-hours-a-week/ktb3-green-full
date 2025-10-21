package com.example.spring_community.User.repository;

import com.example.spring_community.User.domain.UserEntity;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    public UserEntity createNewUser(UserEntity userEntity);
    public UserEntity updateUserInfo(UserEntity userEntity);
    public Optional<UserEntity> findById(Long userId);
    public Optional<UserEntity> findByEmail(String email);
}
