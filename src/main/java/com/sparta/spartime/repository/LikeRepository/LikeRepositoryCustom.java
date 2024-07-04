package com.sparta.spartime.repository.LikeRepository;

import com.sparta.spartime.entity.Like;

public interface LikeRepositoryCustom {
    boolean existsByUserIdAndReferenceTypeAndRefId(Long userId, Like.ReferenceType referenceType, Long refId);
    Like findByUserIdAndReferenceTypeAndRefId(Long userId, Like.ReferenceType referenceType, Long refId);
}
