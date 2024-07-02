package com.sparta.spartime.service;

import com.sparta.spartime.entity.Follow;
import com.sparta.spartime.entity.User;
import com.sparta.spartime.exception.BusinessException;
import com.sparta.spartime.exception.ErrorCode;
import com.sparta.spartime.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserService userService;

    public void follow(Long id, User follower) {
        User following = userService.findById(id);

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
}
