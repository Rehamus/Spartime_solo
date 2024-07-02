package com.sparta.spartime.service.admin;

import com.sparta.spartime.dto.request.PostCreateRequestDto;
import com.sparta.spartime.dto.response.PostResponseDto;
import com.sparta.spartime.entity.Post;
import com.sparta.spartime.entity.User;
import com.sparta.spartime.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminPostService {
    private final PostRepository postRepository;

    @Transactional
    public Object createNoticePost(PostCreateRequestDto requestDto, User user) {
        return new PostResponseDto(postRepository.save(
                Post.builder()
                        .title(requestDto.getTitle())
                        .contents(requestDto.getContents())
                        .type(Post.Type.NOTICE)
                        .user(user)
                        .build())
        );
    }
}
