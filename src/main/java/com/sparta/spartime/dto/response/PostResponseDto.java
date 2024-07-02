package com.sparta.spartime.dto.response;

import com.sparta.spartime.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PostResponseDto {

    private final Long id;
    private final String title;
    private final String content;
    private final Long userId;
    private final Long likes;
    private final String nickname;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContents();
        this.likes = post.getLikes();
        this.userId = post.getUser().getId();
        this.nickname = post.getUser().getNickname();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }

    public PostResponseDto(Post post, Post.Type type) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContents();
        this.likes = post.getLikes();
        this.userId = post.getUser().getId();
        this.nickname = "ANONYMOUS";
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }


}
