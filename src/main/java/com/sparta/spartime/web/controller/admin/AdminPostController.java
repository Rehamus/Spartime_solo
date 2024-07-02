package com.sparta.spartime.web.controller.admin;

import com.sparta.spartime.dto.request.PostCreateRequestDto;
import com.sparta.spartime.entity.User;
import com.sparta.spartime.service.admin.AdminPostService;
import com.sparta.spartime.web.argumentResolver.annotation.LoginUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/posts")
public class AdminPostController {
    private final AdminPostService adminPostService;

    @PostMapping
    public ResponseEntity<?> createNoticePost(
            @Valid @RequestBody PostCreateRequestDto postCreateRequestDto,
            @LoginUser User adminUser) {
        return ResponseEntity.ok(adminPostService.createNoticePost(postCreateRequestDto, adminUser));
    }
}
