package com.sparta.spartime.repository;

import com.sparta.spartime.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmailOrNickname(String email, String nickname);

    Optional<User> findByRefreshToken(String refreshToken);
}
