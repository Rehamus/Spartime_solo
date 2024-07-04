package com.sparta.spartime.repository.LikeRepository;

import com.sparta.spartime.entity.Comment;
import com.sparta.spartime.entity.Post;
import org.springframework.data.domain.Page;

public interface LikeRepositoryCustom {
    Page<Post> getPostsLikedByUser(Long userId, int page, int size,  int asc);
    Page<Comment> getCommentsLikedByUser(Long userId, int page, int size, int asc);
}
