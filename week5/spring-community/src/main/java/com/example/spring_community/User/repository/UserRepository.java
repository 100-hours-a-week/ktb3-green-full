package com.example.spring_community.User.repository;

import com.example.spring_community.Auth.dto.LoginDto;
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
public class UserRepository {
    private static final Path USER_JSON_PATH = Paths.get("src/main/resources/data/users.json");
    private final ObjectMapper objectMapper;
    private final Map<Long, UserEntity> userDatabase = new LinkedHashMap<>();
    private long userId;

    public UserRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void loadFromJson() {
        try(InputStream in = Files.newInputStream(USER_JSON_PATH)) {
            List<UserEntity> userEntityList = objectMapper.readValue(in, new TypeReference<List<UserEntity>>() {});
            userDatabase.clear();
            for (UserEntity user: userEntityList) {
                userDatabase.put(user.getUserId(), user);
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_ACCESS_DB);
        }
        this.userId = userDatabase.keySet().stream().mapToLong(Long::longValue).max().orElse(0L);
    }

    private void saveToJson() {
        try {
            Path temp = Files.createTempFile(USER_JSON_PATH.getParent(), "users-", ".json");
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(temp.toFile(), new ArrayList<>(userDatabase.values()));
            Files.move(temp, USER_JSON_PATH, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_ACCESS_DB);
        }
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
        UserEntity userEntity = findByEmail(loginDto.email())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (!userEntity.getPassword().equals(loginDto.password()) || !userEntity.getActive()) {
            return false;
        }
        return true;
    }
}
