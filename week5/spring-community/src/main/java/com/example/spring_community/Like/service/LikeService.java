package com.example.spring_community.Like.service;

import com.example.spring_community.Like.dto.LikeDto;
import com.example.spring_community.Like.repository.LikeRepository;
import com.example.spring_community.Post.domain.PostEntity;
import com.example.spring_community.Exception.CustomException;
import com.example.spring_community.Exception.ErrorCode;
import com.example.spring_community.Like.repository.LikeJsonRepository;
import com.example.spring_community.Post.repository.PostJsonRepository;
import com.example.spring_community.Post.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    public LikeService(LikeRepository likeRepository, PostRepository postRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
    }

    public LikeDto addLikes(Long postId, Long userId) {
        isValidPostId(postId);
        likeRepository.addLikes(postId, userId);
        int likeCounts = countLikes(postId);
        postRepository.updateLikes(postId, likeCounts);
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        return postEntityToLikeDto(post);
    }

    public LikeDto deleteLikes(Long postId, Long userId) {
        isValidPostId(postId);
        likeRepository.deleteLikes(postId, userId);
        int likeCounts = countLikes(postId);
        postRepository.updateLikes(postId, likeCounts);
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        return postEntityToLikeDto(post);
    }

    public void isValidPostId(Long postId) {
        postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }

    public Integer countLikes(Long postId) {
        Set<Long> likeSet = likeRepository.findLikeUsersList(postId);
        if (likeSet == null) return 0;
        return likeSet.size();
    }

    public boolean isLiked(Long postId, Long userId) {
        Set<Long> likeSet = likeRepository.findLikeUsersList(postId);
        if (likeSet == null) return false;
        return likeSet.contains(userId);
    }

    private LikeDto postEntityToLikeDto(PostEntity postEntity) {
        return LikeDto.builder().postId(postEntity.getPostId()).title(postEntity.getTitle())
                .likes(postEntity.getLikes()).build();
    }

}
