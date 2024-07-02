package com.sparta.spartime.repository;

import com.sparta.spartime.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<List<Comment>> findAllByPostId(Long postId);
    Optional<Comment> findByIdAndPostId(Long id, Long postId);
}
