package com.sparta.spartime.web.controller;

import com.sparta.spartime.aop.envelope.Envelope;
import com.sparta.spartime.entity.User;
import com.sparta.spartime.service.FollowService;
import com.sparta.spartime.web.argumentResolver.annotation.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{id}/follow")
    @Envelope("팔로우가 정상 처리되었습니다.")
    public ResponseEntity<Void> follow(@PathVariable Long id, @LoginUser User follower) {
        followService.follow(id, follower);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}/follow")
    @Envelope("언팔로우가 정상 처리되었습니다.")
    public ResponseEntity<Void> nuFollow(@PathVariable Long id, @LoginUser User follower) {
        followService.unFollow(id, follower);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
