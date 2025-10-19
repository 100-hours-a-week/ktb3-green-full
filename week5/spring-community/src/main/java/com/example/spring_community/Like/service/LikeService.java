package com.example.spring_community.Like.service;

import com.example.spring_community.Like.dto.LikeDto;
import com.example.spring_community.Post.domain.PostEntity;
import com.example.spring_community.Exception.CustomException;
import com.example.spring_community.Exception.ErrorCode;
import com.example.spring_community.Like.repository.LikeRepository;
import com.example.spring_community.Post.repository.PostRepository;
import org.springframework.stereotype.Service;

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
        int likeCounts = likeRepository.countLikes(postId);
        postRepository.updateLikes(postId, likeCounts);
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        return postEntityToLikeDto(post);
    }

    public LikeDto deleteLikes(Long postId, Long userId) {
        isValidPostId(postId);
        likeRepository.deleteLikes(postId, userId);
        int likeCounts = likeRepository.countLikes(postId);
        postRepository.updateLikes(postId, likeCounts);
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        return postEntityToLikeDto(post);
    }

    public boolean isLiked(Long postId, Long userId) {
        isValidPostId(postId);
        return likeRepository.isLiked(postId, userId);
    }

    public void isValidPostId(Long postId) {
        postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }

    private LikeDto postEntityToLikeDto(PostEntity postEntity) {
        return LikeDto.builder().postId(postEntity.getPostId()).title(postEntity.getTitle())
                .likes(postEntity.getLikes()).build();
    }

}
