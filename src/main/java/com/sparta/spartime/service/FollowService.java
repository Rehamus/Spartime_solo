package com.sparta.spartime.service;

import com.sparta.spartime.dto.response.PostResponseDto;
import com.sparta.spartime.dto.response.UserTenResponseDto;
import com.sparta.spartime.entity.Follow;
import com.sparta.spartime.entity.User;
import com.sparta.spartime.exception.BusinessException;
import com.sparta.spartime.exception.ErrorCode;
import com.sparta.spartime.repository.FollowRepository.FollowRepository;
import com.sparta.spartime.searchCond.PostSearchCond;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserService userService;

    public void follow(Long id, User follower) {
        User following = userService.findById(id);

        if (follower.getId().equals(following.getId())) {
            throw new BusinessException(ErrorCode.ALREADY_ME_WHAT);
        }

        if (followRepository.existsByFollowerAndFollowing(follower, following)) {
            throw new BusinessException(ErrorCode.ALREADY_FOLLOW);
        }

        Follow follow = new Follow(follower, following);

        followRepository.save(follow);
    }

    public void unFollow(Long id, User follower) {
        User following = userService.findById(id);

        Follow follow = followRepository.findByFollowerAndFollowing(follower, following).orElseThrow(() ->
                new BusinessException(ErrorCode.NOT_FOLLOWING)
        );

        followRepository.delete(follow);
    }


    public Page<PostResponseDto> followingPost(PostSearchCond searchCond, int page, int size, int asc) {
        return followRepository.getPostsFollowByUser(searchCond ,page, size, asc).map(PostResponseDto::new);
    }

    public Page<UserTenResponseDto> getTopFollowTen() {
        return followRepository.getTopFollowTen();
    }
}
