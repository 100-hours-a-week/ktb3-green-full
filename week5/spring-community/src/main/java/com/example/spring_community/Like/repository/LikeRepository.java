package com.example.spring_community.Like.repository;

import java.util.Set;

public interface LikeRepository {
    public void addLikes(Long postId, Long userId);
    public void deleteLikes(Long postId, Long userId);
    public Set<Long> findLikeUsersList(Long postId);
}
