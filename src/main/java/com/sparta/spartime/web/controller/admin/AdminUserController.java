package com.sparta.spartime.web.controller.admin;

import com.sparta.spartime.dto.request.AdminUserRoleUpdateDto;
import com.sparta.spartime.dto.request.AdminUserStatusUpdateDto;
import com.sparta.spartime.service.admin.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {
    private final AdminUserService adminUserService;

    // 유저 조회
    @GetMapping("/{userId}")
    public ResponseEntity<?> showUser(@PathVariable Long userId) {
        return ResponseEntity.ok(adminUserService.getUserById(userId));
    }

    // 유저 권한 변경 & 유저 상태 변경
    @PatchMapping("/{userId}/role")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody AdminUserRoleUpdateDto requestDto) {
        return ResponseEntity.ok(adminUserService.updateUserRole(userId, requestDto));
    }

    @PatchMapping("/{userId}/status")
    public ResponseEntity<?> updateUserStatus(@PathVariable Long userId, @RequestBody AdminUserStatusUpdateDto requestDto) {
        return ResponseEntity.ok(adminUserService.updateUserStatus(userId, requestDto));
    }
}
