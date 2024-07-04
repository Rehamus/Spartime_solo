package com.sparta.spartime.repository.LikeRepository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.spartime.entity.Like;
import com.sparta.spartime.entity.QLike;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class LikeRepositoryCustomImpl implements LikeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByUserIdAndReferenceTypeAndRefId(Long userId, Like.ReferenceType referenceType, Long refId) {
        QLike like = QLike.like;

        return queryFactory
                .selectFrom(like)
                .where(like.user.id.eq(userId)
                               .and(like.referenceType.eq(referenceType))
                               .and(like.refId.eq(refId)))
                .fetchFirst() != null;
    }

    @Override
    public Like findByUserIdAndReferenceTypeAndRefId(Long userId, Like.ReferenceType referenceType, Long refId) {
        QLike like = QLike.like;

        return queryFactory
                .selectFrom(like)
                .where(like.user.id.eq(userId)
                               .and(like.referenceType.eq(referenceType))
                               .and(like.refId.eq(refId)))
                .fetchOne();
    }
}
