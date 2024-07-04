package com.sparta.spartime.repository.LikeRepository;

import com.sparta.spartime.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long>, LikeRepositoryCustom {
}
