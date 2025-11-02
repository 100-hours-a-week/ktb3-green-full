package com.example.spring_community.Like.service;

import com.example.spring_community.Like.domain.LikeEntity;
import com.example.spring_community.Like.dto.LikeDto;
import com.example.spring_community.Like.repository.LikeRepository;
import com.example.spring_community.Post.domain.PostEntity;
import com.example.spring_community.Exception.CustomException;
import com.example.spring_community.Exception.ErrorCode;
import com.example.spring_community.Post.domain.PostMetaEntity;
import com.example.spring_community.Post.repository.PostMetaRepository;
import com.example.spring_community.Post.repository.PostRepository;
import com.example.spring_community.User.domain.UserEntity;
import com.example.spring_community.User.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMetaRepository postMetaRepository;

    public LikeService(LikeRepository likeRepository, PostRepository postRepository, UserRepository userRepository, PostMetaRepository postMetaRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postMetaRepository = postMetaRepository;
    }

    @Transactional
    public LikeDto addLikes(Long postId, Long userId) {

        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        LikeEntity likeEntity = LikeEntity.builder()
                .post(postEntity).user(userEntity)
                .build();

        likeRepository.save(likeEntity);

        PostMetaEntity postMetaEntity = postMetaRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        postMetaEntity.increaseLikeCount();
        postMetaRepository.save(postMetaEntity);

        return LikeDto.of(postEntity);
    }

    @Transactional
    public LikeDto deleteLikes(Long postId, Long userId) {

        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        LikeEntity likeEntity = isLiked(postId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.LIKE_NOT_FOUND));

        likeRepository.delete(likeEntity);

        PostMetaEntity postMetaEntity = postMetaRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        postMetaEntity.decreaseLikeCount();
        postMetaRepository.save(postMetaEntity);

        return LikeDto.of(postEntity);
    }

    public Optional<LikeEntity> isLiked(Long postId, Long userId) {

        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return likeRepository.findByPostAndUser(postEntity, userEntity);
    }

    public boolean isLike(Long postId, Long userId) {
        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return likeRepository.existsByPostAndUser(postEntity, userEntity);
    }

}
