package com.example.spring_community.Post.repository;

import com.example.spring_community.Post.domain.PostEntity;
import java.util.List;
import java.util.Optional;

public interface PostRepository {
    public int countAllPosts();
    public Optional<PostEntity> findById(Long postId);
    public List<PostEntity> findPostPage(int page, int size);
    public PostEntity createPost(PostEntity newPostEntity);
    public PostEntity updatePost(PostEntity updatePostEntity);
    public void deleteByPostId(Long postId);
    public void updateLikes(Long postId, int likesCount);

}
