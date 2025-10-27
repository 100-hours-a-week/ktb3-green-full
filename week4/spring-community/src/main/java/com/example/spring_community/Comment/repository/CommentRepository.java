package com.example.spring_community.Comment.repository;

import com.example.spring_community.Comment.domain.CommentEntity;
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
public class CommentRepository {
    private static final Path COMMENT_JSON_PATH = Paths.get("src/main/resources/data/comments.json");
    private final ObjectMapper objectMapper;
    private final Map<Long, CommentEntity> commentDatabase = new LinkedHashMap<>();
    private long commentId;

    public CommentRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void loadFromJson() {
        try(InputStream in = Files.newInputStream(COMMENT_JSON_PATH)) {
            List<CommentEntity> commentEntityList = objectMapper.readValue(in, new TypeReference<List<CommentEntity>>() {});
            commentDatabase.clear();
            for (CommentEntity comment: commentEntityList) {
                commentDatabase.put(comment.getPostId(), comment);
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_ACCESS_DB);
        }
        this.commentId = commentDatabase.keySet().stream().mapToLong(Long::longValue).max().orElse(0L);
    }

    private void saveToJson() {
        try {
            Path temp = Files.createTempFile(COMMENT_JSON_PATH.getParent(), "comments-", ".json");
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(temp.toFile(), new ArrayList<>(commentDatabase.values()));
            Files.move(temp, COMMENT_JSON_PATH, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_ACCESS_DB);
        }
    }

    public List<CommentEntity> findAllComments(Long postId) {
        return commentDatabase.values().stream()
                .filter(c -> Objects.equals(c.getPostId(), postId))
                .sorted(Comparator.comparing(CommentEntity::getCreatedAt).reversed())
                .toList();
    }

    public Optional<CommentEntity> findByCommentId(Long commentId) {
        return Optional.ofNullable(commentDatabase.get(commentId));
    }

    public synchronized CommentEntity createComment(CommentEntity newCommentEntity) {
        newCommentEntity.setCommentId(++commentId);
        commentDatabase.put(newCommentEntity.getCommentId(), newCommentEntity);
        saveToJson();
        return commentDatabase.get(newCommentEntity.getCommentId());
    }

    public synchronized CommentEntity updateComment(CommentEntity updateCommentEntity) {
        updateCommentEntity.setCommentId(updateCommentEntity.getCommentId());
        commentDatabase.put(updateCommentEntity.getCommentId(), updateCommentEntity);
        saveToJson();
        return commentDatabase.get(updateCommentEntity.getCommentId());
    }

    public synchronized void deleteByCommentId(Long commentId) {
        if (commentDatabase.remove(commentId) == null) {
            throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
        }
        saveToJson();
    }
}
