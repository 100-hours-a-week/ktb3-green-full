package com.example.spring_community.Like.repository;

import com.example.spring_community.Like.domain.LikeEntity;
import com.example.spring_community.Post.domain.PostEntity;
import com.example.spring_community.User.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    boolean existsByPostAndUser(PostEntity post, UserEntity user);
    Optional<LikeEntity> findByPostAndUser(PostEntity post, UserEntity user);
}
