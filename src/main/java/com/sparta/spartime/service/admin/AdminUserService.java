package com.sparta.spartime.service.admin;

import com.sparta.spartime.dto.request.AdminUserRoleUpdateDto;
import com.sparta.spartime.dto.request.AdminUserStatusUpdateDto;
import com.sparta.spartime.dto.response.UserResponseDto;
import com.sparta.spartime.entity.User;
import com.sparta.spartime.exception.BusinessException;
import com.sparta.spartime.exception.ErrorCode;
import com.sparta.spartime.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUserService {
    private final UserRepository userRepository;

    @Transactional
    public UserResponseDto updateUserRole(Long targetUserId, AdminUserRoleUpdateDto role) {
        User user = userRepository.findById(targetUserId).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        user.changeRole(User.Role.valueOf(role.getRole()));
        return new UserResponseDto(user);
    }

    @Transactional
    public UserResponseDto updateUserStatus(Long targetUserId, AdminUserStatusUpdateDto status) {
        User user = userRepository.findById(targetUserId).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        user.changeStatus(User.Status.valueOf(status.getStatus()));
        return new UserResponseDto(user);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }
}

