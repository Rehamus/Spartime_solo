package com.sparta.spartime.web.controller;

import com.sparta.spartime.aop.envelope.Envelope;
import com.sparta.spartime.dto.response.PostResponseDto;
import com.sparta.spartime.dto.response.UserTenResponseDto;
import com.sparta.spartime.entity.Post;
import com.sparta.spartime.entity.User;
import com.sparta.spartime.searchCond.PostSearchCond;
import com.sparta.spartime.service.FollowService;
import com.sparta.spartime.web.argumentResolver.annotation.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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


    @GetMapping("/folloing/posts")
    public ResponseEntity<Page<PostResponseDto>> folloingPost(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String contents,
            @RequestParam(required = false) Post.Type type,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "0") int asc,
            @LoginUser User follower) {

        PostSearchCond searchCond = new PostSearchCond();
        searchCond.setUserId(follower.getId());
        searchCond.setTitle(title);
        searchCond.setContents(contents);
        searchCond.setType(type);

        return ResponseEntity.ok(followService.followingPost(searchCond, page - 1, size, asc));
    }

    @GetMapping("/follow/topten")
    public ResponseEntity<Page<UserTenResponseDto>> folloingPost() {
        return ResponseEntity.ok(followService.getTopFollowTen());
    }
}
