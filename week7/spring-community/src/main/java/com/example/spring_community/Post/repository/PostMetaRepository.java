package com.example.spring_community.Post.repository;

import com.example.spring_community.Post.domain.PostMetaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostMetaRepository extends JpaRepository<PostMetaEntity, Long> {
    @Query("update PostMetaEntity m set m.likeCount = m.likeCount + 1 where m.postId = :postId")
    int incrementLike(@Param("postId") Long postId);

    @Query("update PostMetaEntity m set m.likeCount = case when m.likeCount > 0 then m.likeCount - 1 else 0 end " +
            "where m.postId = :postId")
    int decrementLike(@Param("postId") Long postId);
}
