package com.example.spring_community.Comment.repository;

import com.example.spring_community.Comment.domain.CommentEntity;
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
public class CommentJsonRepository extends JsonRepository<CommentEntity> implements CommentRepository{
    private static final Path COMMENT_JSON_PATH = Paths.get("src/main/resources/data/comments.json");
    private final Map<Long, CommentEntity> commentDatabase = new ConcurrentHashMap<>();
    private long commentId;

    public CommentJsonRepository(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @PostConstruct
    public void loadFromJson() {
        List<CommentEntity> commentEntityList = loadListFromJson(COMMENT_JSON_PATH, new TypeReference<List<CommentEntity>>() {});
        commentDatabase.clear();
        for (CommentEntity comment: commentEntityList) commentDatabase.put(comment.getPostId(), comment);
        this.commentId = commentDatabase.keySet().stream().mapToLong(Long::longValue).max().orElse(0L);
    }

    public void saveToJson() {
        List<CommentEntity> commentEntityList = new ArrayList<>(commentDatabase.values());
        saveListToJson(COMMENT_JSON_PATH, "comments", commentEntityList);
    }

    public List<CommentEntity> findAllComments(Long postId) {
        return commentDatabase.values().stream()
                .filter(c -> Objects.equals(c.getPostId(), postId))
                .sorted(Comparator.comparing(CommentEntity::getCreatedAt).reversed())
                .toList();
    }

    public Optional<CommentEntity> findById(Long commentId) {
        return Optional.ofNullable(commentDatabase.get(commentId));
    }

    public CommentEntity createComment(CommentEntity newCommentEntity) {
        newCommentEntity.setCommentId(++commentId);
        commentDatabase.put(newCommentEntity.getCommentId(), newCommentEntity);
        saveToJson();
        return commentDatabase.get(newCommentEntity.getCommentId());
    }

    public CommentEntity updateComment(CommentEntity updateCommentEntity) {
        updateCommentEntity.setCommentId(updateCommentEntity.getCommentId());
        commentDatabase.put(updateCommentEntity.getCommentId(), updateCommentEntity);
        saveToJson();
        return commentDatabase.get(updateCommentEntity.getCommentId());
    }

    public void deleteByCommentId(Long commentId) {
        if (commentDatabase.remove(commentId) == null) {
            throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
        }
        saveToJson();
    }
}
