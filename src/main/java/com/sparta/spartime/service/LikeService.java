package com.sparta.spartime.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.spartime.entity.Like;
import com.sparta.spartime.entity.QLike;
import com.sparta.spartime.entity.User;
import com.sparta.spartime.exception.BusinessException;
import com.sparta.spartime.exception.ErrorCode;
import com.sparta.spartime.repository.LikeRepository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final JPAQueryFactory queryFactory;

    public void like(User user, Like.ReferenceType refType, Long refId) {
        Like like = findLikeBy(user.getId(), refType, refId);
        if (like != null) {
            throw new BusinessException(ErrorCode.LIKE_ALREADY_EXISTS);
        }

        likeRepository.save(
                Like.builder()
                        .refId(refId)
                        .user(user)
                        .referenceType(refType)
                        .build()
        );
    }

    public void unlike(User user, Like.ReferenceType refType, Long refId) {
        Like like = findLikeBy(user.getId(), refType, refId);
        if (like == null) {
            throw new BusinessException(ErrorCode.LIKE_NOT_FOUND);
        }
        likeRepository.delete(like);
    }


    public boolean hasUserLikedPost(Long userId, Long postId) {
        QLike qLike = QLike.like;

        return queryFactory
                .selectFrom(qLike)
                .where(qLike.user.id.eq(userId)
                               .and(qLike.referenceType.eq(Like.ReferenceType.POST))
                               .and(qLike.refId.eq(postId)))
                .fetchFirst() != null;
    }

    private Like findLikeBy(Long userId, Like.ReferenceType refType, Long refId) {
        QLike qLike = QLike.like;

        return Optional.ofNullable(
                queryFactory
                        .selectFrom(qLike)
                        .where(qLike.user.id.eq(userId)
                                       .and(qLike.referenceType.eq(refType))
                                       .and(qLike.refId.eq(refId)))
                        .fetchOne()
        ).orElse(null);
    }
}
