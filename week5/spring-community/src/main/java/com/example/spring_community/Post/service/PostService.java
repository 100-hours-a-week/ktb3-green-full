package com.example.spring_community.Post.service;

import com.example.spring_community.Exception.CustomException;
import com.example.spring_community.Exception.ErrorCode;
import com.example.spring_community.Post.dto.*;
import com.example.spring_community.Post.domain.PostEntity;
import com.example.spring_community.Post.repository.PostJsonRepository;
import com.example.spring_community.Post.repository.PostRepository;
import com.example.spring_community.User.domain.Author;
import com.example.spring_community.User.domain.UserEntity;
import com.example.spring_community.User.repository.UserJsonRepository;
import com.example.spring_community.User.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostJsonRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public DetailPostDto readPost(Long postId) {
        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        return entityToDetailDto(postEntity);
    }

    public PagePostDto<PostDto> readPostPage(int page, int size) {
        List<PostEntity> postPageList = postRepository.findPostPage(page, size);
        int totalPosts = postRepository.countAllPosts();
        List<PostDto> postPageItems = postPageList.stream().map(this::entityToDto).toList();
        return PagePostDto.of(postPageItems, page, size, totalPosts);
    }

    public PostDto createPost(Long userId, NewPostDto newPostDto) {
        UserEntity postUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Author postAuthor = new Author(userId, postUser.getNickname());
        Instant now = Instant.now();
        PostEntity newPostEntity = PostEntity.builder()
                .title(newPostDto.getTitle())
                .content(newPostDto.getContent())
                .postImg(newPostDto.getPostImg())
                .author(postAuthor)
                .likes(0).views(0).comments(0)
                .createdAt(now).updatedAt(now).build();
        PostEntity createdPostEntity = postRepository.createPost(newPostEntity);
        return entityToDto(createdPostEntity);
    }

    public NewPostDto updatePost(Long userId, Long postId, UpdatePostDto updatePostDto) {
        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        if (!userId.equals(postEntity.getAuthor().getUserId())) {
            throw new CustomException(ErrorCode.POST_UPDATE_FORBIDDEN_USER);
        }
        Instant now = Instant.now();

        String updateTitle = Optional.ofNullable(updatePostDto.getTitle())
                .map(String::trim).filter(s -> !s.isEmpty()).orElse(postEntity.getTitle());

        String updateContent = Optional.ofNullable(updatePostDto.getContent())
                .map(String::trim).filter(s -> !s.isEmpty()).orElse(postEntity.getContent());

        String updatePostImg = Optional.ofNullable(updatePostDto.getPostImg())
                .map(String::trim).filter(s -> !s.isEmpty()).orElse(postEntity.getPostImg());

        PostEntity newPostEntity = postEntity.toBuilder()
                .title(updateTitle)
                .content(updateContent)
                .postImg(updatePostImg)
                .updatedAt(now).build();

        PostEntity updatedPost = postRepository.updatePost(newPostEntity);
        NewPostDto updatedPostDto = NewPostDto.builder()
                .title(updatedPost.getTitle())
                .content(updatedPost.getContent())
                .postImg(updatedPost.getPostImg()).build();
        return updatedPostDto;
    }

    public void deletePost(Long userId, Long postId) {
        UserEntity deleteUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        if (!userId.equals(postEntity.getAuthor().getUserId())) {
            throw new CustomException(ErrorCode.POST_DELETE_FORBIDDEN_USER);
        }
        postRepository.deleteByPostId(postId);
    }

    public PostDto entityToDto(PostEntity postEntity) {
        return PostDto.builder()
                .postId(postEntity.getPostId())
                .title(postEntity.getTitle())
                .author(postEntity.getAuthor())
                .likes(postEntity.getLikes()).views(postEntity.getViews()).comments(postEntity.getComments())
                .createdAt(postEntity.getCreatedAt()).updatedAt(postEntity.getUpdatedAt())
                .build();
    }

    public DetailPostDto entityToDetailDto(PostEntity postEntity) {
        return DetailPostDto.builder()
                .postId(postEntity.getPostId())
                .title(postEntity.getTitle())
                .content(postEntity.getContent())
                .postImg(postEntity.getPostImg())
                .author(postEntity.getAuthor())
                .likes(postEntity.getLikes()).views(postEntity.getViews()).comments(postEntity.getComments())
                .createdAt(postEntity.getCreatedAt()).updatedAt(postEntity.getUpdatedAt())
                .build();
    }
}
