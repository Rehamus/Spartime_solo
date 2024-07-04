package com.sparta.spartime.repository.LikeRepository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.spartime.entity.*;
import com.sparta.spartime.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class LikeRepositoryCustomImpl implements LikeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Post> getPostsLikedByUser(Long userId, int page, int size, int asc) {
        QLike like = QLike.like;
        QPost post = QPost.post;
        OrderSpecifier<?> orderBy = asc == 0 ? post.createdAt.asc() : post.createdAt.desc();

        List<Post> posts = queryFactory
                .select(post)
                .from(like)
                .innerJoin(post).on(like.refId.eq(post.id)
                                               .and(like.referenceType.eq(Like.ReferenceType.POST)))
                .where(like.user.id.eq(userId))
                .orderBy(orderBy)
                .offset((long) page * size)
                .limit(size)
                .fetch();

        Long totalCount = queryFactory
                .select(like.count())
                .from(like)
                .where(like.user.id.eq(userId)
                               .and(like.referenceType.eq(Like.ReferenceType.POST)))
                .fetchOne();

        long count = totalCount != null ? totalCount : 0L;

        return PageableExecutionUtils.getPage(posts, PageRequest.of(page, size), () -> count);
    }

    @Override
    public Page<Comment> getCommentsLikedByUser(Long userId, int page, int size , int asc) {
        QLike like = QLike.like;
        QComment comment = QComment.comment;
        OrderSpecifier<?> orderBy = asc == 0 ? comment.createdAt.asc() : comment.createdAt.desc();


        List<Comment> comments = queryFactory
                .select(comment)
                .from(like)
                .innerJoin(comment).on(like.refId.eq(comment.id)
                                               .and(like.referenceType.eq(Like.ReferenceType.COMMENT)))
                .where(like.user.id.eq(userId))
                .orderBy(orderBy)
                .offset((long) page * size)
                .limit(size)
                .fetch();

        Long totalCount = queryFactory
                .select(like.count())
                .from(like)
                .where(like.user.id.eq(userId)
                               .and(like.referenceType.eq(Like.ReferenceType.COMMENT)))
                .fetchOne();

        long count = totalCount != null ? totalCount : 0L;

        return PageableExecutionUtils.getPage(comments, PageRequest.of(page, size), () -> count);
    }

}
