package com.example.spring_community.Post.repository;

import com.example.spring_community.Post.domain.PostEntity;
import com.example.spring_community.Exception.CustomException;
import com.example.spring_community.Exception.ErrorCode;
import com.example.spring_community.Storage.JsonRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class PostJsonRepository extends JsonRepository<PostEntity> implements PostRepository{
    private static final Path POST_JSON_PATH = Paths.get("src/main/resources/data/posts.json");
    private final Map<Long, PostEntity> postDatabase = new ConcurrentHashMap<>();
    private Long postId;

    public PostJsonRepository(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @PostConstruct
    @Override
    public void loadFromJson() {
        List<PostEntity> postEntityList = loadListFromJson(POST_JSON_PATH, new TypeReference<List<PostEntity>>() {});
        postDatabase.clear();
        for (PostEntity post: postEntityList) postDatabase.put(post.getPostId(), post);
        this.postId = postDatabase.keySet().stream().mapToLong(Long::longValue).max().orElse(0L);
    }

    @Override
    public void saveToJson() {
        List<PostEntity> postEntityList = new ArrayList<>(postDatabase.values());
        saveListToJson(POST_JSON_PATH, "posts", postEntityList);
    }

    public int countAllPosts() {
        return postDatabase.size();
    }

    public Optional<PostEntity> findById(Long postId) {
        return Optional.ofNullable(postDatabase.get(postId));
    }

    public List<PostEntity> findPostPage(int page, int size) {
        List<PostEntity> allPosts = new ArrayList<>(this.postDatabase.values());
        allPosts.sort(null);
        int totalPosts = allPosts.size();
        int from = Math.min(page * size, totalPosts);
        int to = Math.min(from + size, totalPosts);
        List<PostEntity> postPage = allPosts.subList(from, to);
        return postPage;
    }

    public PostEntity createPost(PostEntity newPostEntity) {
        newPostEntity.setPostId(++postId);
        postDatabase.put(newPostEntity.getPostId(), newPostEntity);
        saveToJson();
        return newPostEntity;
    }

    public PostEntity updatePost(PostEntity updatePostEntity) {
        updatePostEntity.setPostId(postId);
        postDatabase.put(updatePostEntity.getPostId(), updatePostEntity);
        saveToJson();
        return updatePostEntity;
    }

    public void deleteByPostId(Long postId) {
        if (postDatabase.remove(postId) == null) {
            throw new CustomException(ErrorCode.POST_NOT_FOUND);
        }
        saveToJson();
    }

    public void updateLikes(Long postId, int likesCount) {
        PostEntity post = postDatabase.get(postId);
        if (post == null) { throw new CustomException(ErrorCode.POST_NOT_FOUND); }
        PostEntity updatedPost = post.toBuilder().likes(likesCount).build();
        postDatabase.put(postId, updatedPost);
        saveToJson();
    }


}
