package com.sparta.spartime.service;

import com.sparta.spartime.dto.request.PostRequestDto;
import com.sparta.spartime.dto.response.PostResponseDto;
import com.sparta.spartime.entity.Like;
import com.sparta.spartime.entity.Post;
import com.sparta.spartime.entity.User;
import com.sparta.spartime.exception.BusinessException;
import com.sparta.spartime.exception.ErrorCode;
import com.sparta.spartime.repository.LikeRepository.LikeRepository;
import com.sparta.spartime.repository.PostRepository;
import com.sparta.spartime.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {
    
    private final PostRepository postrepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final LikeService likeService;


    public PostResponseDto create(PostRequestDto requestDto, User user ) {
        Post post = new Post(requestDto,Post.Type.NORMAL,user);
        postrepository.save(post);
        return new PostResponseDto(post);
    }

    public PostResponseDto createAnonymous(PostRequestDto requestDto ,User user) {
        Post post = new Post(requestDto, Post.Type.ANONYMOUS, user);
        postrepository.save(post);
        return new PostResponseDto(post,Post.Type.ANONYMOUS );
    }

    public Page<PostResponseDto> getPage(int page, int size, String type) {
        PageRequest pageRequest = PageRequest.of(page, size);
        if (!type.isEmpty()) {
            Post.Type postType = Post.Type.valueOf(type.toUpperCase());
            return postrepository.findByType(postType, pageRequest).map(post -> new PostResponseDto(post, postType));
        } else {
            return postrepository.findAll(pageRequest).map(PostResponseDto::new);
        }
    }



    public PostResponseDto get(Long postId) {
        Post post = getPost(postId);
        if (post.getType() == Post.Type.ANONYMOUS) {
            return new PostResponseDto(post,Post.Type.ANONYMOUS);
        }
        return new PostResponseDto(post);
    }


    @Transactional
    public PostResponseDto update(PostRequestDto requestDto,Long postId,User user) {
        Post post = getPost(postId);
        userCheck(user, post);

        post.update(requestDto);
        return new PostResponseDto(post);
    }

    @Transactional
    public void delete(Long postId,User user) {
        Post post = getPost(postId);
        userCheck(user, post);
        postrepository.delete(post);
    }

    @Transactional
    public void like(Long postId,User user) {
        Post post = getPost(postId);
        if (user.getId().equals(post.getUser().getId())){
            throw new BusinessException(ErrorCode.LIKE_NO_MY_FOUND);
        }
        likeService.like(user, Like.ReferenceType.POST, postId);
        liked(post, 1L);
    }

    @Transactional
    public void unlike(Long postId,User user) {
        Post post = getPost(postId);
        likeService.unlike(user, Like.ReferenceType.POST, postId);
        liked(post, -1L);

    }


    public Page<PostResponseDto> getLikePage( Long user,int page, int size,int asc) {
        return likeRepository.getPostsLikedByUser(user,page,size,asc).map(PostResponseDto::new);
    }


    //:::::::::::::::::::// 도구 //::::::::::::::::::://

    public Post getPost(Long postId) {
        return postrepository.findById(postId).orElseThrow(
                () -> new BusinessException(ErrorCode.POST_NOT_FOUND)
        );
    }

    @Transactional
    public void liked(Post post, Long num) {
        User user = userRepository.findById(post.getUser().getId()).orElseThrow(null);
        user.setpostLiked(user.getPostLiked() + num);
        post.Likes(post.getLikes()+num);
    }

    private void userCheck(User user, Post post) {
        if(!post.getUser().getId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.POST_NOT_USER);
        }
    }
}
