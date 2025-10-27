package com.example.spring_community.Like.repository;

import com.example.spring_community.Exception.CustomException;
import com.example.spring_community.Exception.ErrorCode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class LikeJsonRepository implements LikeRepository{
    private static final Path LIKE_JSON_PATH = Paths.get("src/main/resources/data/likes.json");
    private final ObjectMapper objectMapper;
    private final Map<Long, Set<Long>> likeDatabase = new ConcurrentHashMap<>();

    public LikeJsonRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void loadFromJson() {
        try(InputStream in = Files.newInputStream(LIKE_JSON_PATH)) {
            Map<Long, Set<Long>> likeList = objectMapper.readValue(in, new TypeReference<Map<Long, Set<Long>>>() {});
            likeDatabase.clear();
            for (Map.Entry<Long, Set<Long>> like: likeList.entrySet()) {
                Set<Long> threadSafeSet = ConcurrentHashMap.newKeySet();
                if (like.getValue() != null) threadSafeSet.addAll(like.getValue());
                likeDatabase.put(like.getKey(), threadSafeSet);
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_ACCESS_DB);
        }
    }

    private void saveToJson() {
        try {
            Path temp = Files.createTempFile(LIKE_JSON_PATH.getParent(), "posts-", ".json");
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(temp.toFile(), likeDatabase);
            Files.move(temp, LIKE_JSON_PATH, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_ACCESS_DB);
        }
    }
    public void addLikes(Long postId, Long userId) {
        Set<Long> likeSet = likeDatabase.computeIfAbsent(postId, key -> ConcurrentHashMap.newKeySet());
        if (likeSet.contains(userId)) {
            throw new CustomException(ErrorCode.DUPLICATED_LIKE);
        }
        boolean add = likeSet.add(userId);
        if(add) saveToJson();
    }

    public void deleteLikes(Long postId, Long userId) {
        Set<Long> likeSet = likeDatabase.get(postId);
        if(likeSet == null || !likeSet.contains(userId)) {
            throw new CustomException(ErrorCode.LIKE_NOT_FOUND);
        }
        boolean delete = likeSet.remove(userId);
        if(delete) saveToJson();
    }

    public Set<Long> findLikeUsersList(Long postId) {
        return likeDatabase.get(postId);
    }

 }
