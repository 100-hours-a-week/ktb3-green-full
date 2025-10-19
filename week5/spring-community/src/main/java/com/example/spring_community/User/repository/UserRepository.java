package com.example.spring_community.User.repository;

import com.example.spring_community.Auth.dto.LoginDto;
import com.example.spring_community.Storage.JsonRepository;
import com.example.spring_community.User.domain.UserEntity;
import com.example.spring_community.Exception.CustomException;
import com.example.spring_community.Exception.ErrorCode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.io.InputStream;
import java.nio.file.*;
import java.util.*;

@Repository
public class UserRepository extends JsonRepository<UserEntity> {
    private static final Path USER_JSON_PATH = Paths.get("src/main/resources/data/users.json");
    private final Map<Long, UserEntity> userDatabase = new LinkedHashMap<>();
    private long userId;

    public UserRepository(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @PostConstruct
    public void loadFromJson() {
        List<UserEntity> userEntityList = loadListFromJson(USER_JSON_PATH, new TypeReference<List<UserEntity>>() {});
        userDatabase.clear();
        for (UserEntity user: userEntityList) userDatabase.put(user.getUserId(), user);
        this.userId = userDatabase.keySet().stream().mapToLong(Long::longValue).max().orElse(0L);
    }

    private void saveToJson() {
        List<UserEntity> userEntityList = new ArrayList<>(userDatabase.values());
        saveListToJson(USER_JSON_PATH, "users-", userEntityList);
    }

    public UserEntity createNewUser(UserEntity userEntity) {
        userEntity.setUserId(++userId);
        userDatabase.put(userEntity.getUserId(), userEntity);
        saveToJson();
        return userEntity;
    }

    public UserEntity updateUserInfo(UserEntity userEntity) {
        userDatabase.put(userEntity.getUserId(), userEntity);
        saveToJson();
        return userDatabase.get(userEntity.getUserId());
    }

    public Optional<UserEntity> findById(Long userId) {
        return Optional.ofNullable(userDatabase.get(userId));
    }

    public Optional<UserEntity> findByEmail(String email) {
        return userDatabase.values().stream()
                .filter(u -> u.getEmail() != null && u.getEmail().equals(email))
                .findFirst();
    }

    public boolean isValidUser(LoginDto loginDto) {
        UserEntity userEntity = findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (!userEntity.getPassword().equals(loginDto.getPassword()) || !userEntity.getActive()) {
            return false;
        }
        return true;
    }
}
