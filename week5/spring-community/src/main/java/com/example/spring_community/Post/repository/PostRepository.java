package com.example.spring_community.Post.repository;

import com.example.spring_community.Post.domain.PostEntity;
import com.example.spring_community.Exception.CustomException;
import com.example.spring_community.Exception.ErrorCode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.io.InputStream;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class PostRepository {
    private static final Path POST_JSON_PATH = Paths.get("src/main/resources/data/posts.json");
    private final ObjectMapper objectMapper;
    private final Map<Long, PostEntity> postDatabase = new ConcurrentHashMap<>();
    private Long postId;

    public PostRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void loadFromJson() {
        try(InputStream in = Files.newInputStream(POST_JSON_PATH)) {
            List<PostEntity> postEntityList = objectMapper.readValue(in, new TypeReference<List<PostEntity>>() {});
            postDatabase.clear();
            for (PostEntity post: postEntityList) {
                postDatabase.put(post.getPostId(), post);
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_ACCESS_DB);
        }
        this.postId = postDatabase.keySet().stream().mapToLong(Long::longValue).max().orElse(0L);
    }

    private void saveToJson() {
        try {
            Path temp = Files.createTempFile(POST_JSON_PATH.getParent(), "posts-", ".json");
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(temp.toFile(), new ArrayList<>(postDatabase.values()));
            Files.move(temp, POST_JSON_PATH, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_ACCESS_DB);
        }
    }

    public synchronized int countAllPosts() {
        return postDatabase.size();
    }

    public Optional<PostEntity> findById(Long postId) {
        return Optional.ofNullable(postDatabase.get(postId));
    }

    public synchronized List<PostEntity> findPostPage(int page, int size) {
        List<PostEntity> allPosts = new ArrayList<>(this.postDatabase.values());
        allPosts.sort(null);
        int totalPosts = allPosts.size();
        int from = Math.min(page * size, totalPosts);
        int to = Math.min(from + size, totalPosts);
        List<PostEntity> postPage = allPosts.subList(from, to);
        return postPage;
    }

    public synchronized PostEntity createPost(PostEntity newPostEntity) {
        newPostEntity.setPostId(++postId);
        postDatabase.put(newPostEntity.getPostId(), newPostEntity);
        saveToJson();
        return newPostEntity;
    }

    public synchronized PostEntity updatePost(PostEntity updatePostEntity) {
        updatePostEntity.setPostId(postId);
        postDatabase.put(updatePostEntity.getPostId(), updatePostEntity);
        saveToJson();
        return updatePostEntity;
    }

    public synchronized void deleteByPostId(Long postId) {
        if (postDatabase.remove(postId) == null) {
            throw new CustomException(ErrorCode.POST_NOT_FOUND);
        }
        saveToJson();
    }

    public synchronized void updateLikes(Long postId, int likesCount) {
        PostEntity post = postDatabase.get(postId);
        if (post == null) { throw new CustomException(ErrorCode.POST_NOT_FOUND); }
        PostEntity updatedpost = post.toBuilder()
                .likes(likesCount).build();
        postDatabase.put(postId, updatedpost);
        saveToJson();
    }


}
