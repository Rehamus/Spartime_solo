package com.sparta.spartime.repository.FollowRepository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.spartime.dto.response.UserTenResponseDto;
import com.sparta.spartime.entity.*;
import com.sparta.spartime.entity.Post;
import com.sparta.spartime.searchCond.PostSearchCond;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class FollowRepositoryCustomImpl implements FollowRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Post> getPostsFollowByUser(PostSearchCond searchCond, int page, int size, int asc) {
        QFollow follow = QFollow.follow;
        QPost post = QPost.post;

        // 정렬 방향 설정
        OrderSpecifier<?> orderBy;
        switch (asc) {
            case 1 -> orderBy = post.createdAt.desc();
            case 2 -> orderBy = post.user.nickname.asc();
            case 3 -> orderBy = post.user.nickname.desc();
            default -> orderBy = post.createdAt.asc();
        }

        // 사용자가 팔로우한 포스트 가져오기
        List<Post> posts = queryFactory
                .select(post)
                .from(follow)
                .innerJoin(post).on(follow.following.id.eq(post.user.id))
                .where(
                        follow.follower.id.eq(searchCond.getUserId()),
                        titleContains(searchCond.getTitle()),
                        contentsContains(searchCond.getContents()),
                        typeEq(searchCond.getType())
                )
                .orderBy(orderBy)
                .offset((long) page * size)
                .limit(size)
                .fetch();

        Long totalCount = queryFactory
                .select(post.count())
                .from(follow)
                .innerJoin(post).on(follow.following.id.eq(post.user.id))
                .where(
                        follow.follower.id.eq(searchCond.getUserId()),
                        titleContains(searchCond.getTitle()),
                        contentsContains(searchCond.getContents()),
                        typeEq(searchCond.getType())
                )
                .fetchOne();

        long count = totalCount != null ? totalCount : 0L;

        return PageableExecutionUtils.getPage(posts, PageRequest.of(page, size), () -> count);
    }

    private BooleanExpression titleContains(String title) {
        return title != null ? QPost.post.title.contains(title) : null;
    }

    private BooleanExpression contentsContains(String contents) {
        return contents != null ? QPost.post.contents.contains(contents) : null;
    }

    private BooleanExpression typeEq(Post.Type type) {
        return type != null ? QPost.post.type.eq(type) : null;
    }




    @Override
    public Page<UserTenResponseDto> getTopFollowTen() {
        QFollow follow = QFollow.follow;
        QUser user = QUser.user;
        int page = 0;
        int size = 10;

        NumberExpression<Long> followerCount = follow.count();
        List<UserTenResponseDto> results = queryFactory
                .select(Projections.constructor(UserTenResponseDto.class,
                                                user.id,
                                                user.email,
                                                user.nickname,
                                                user.intro,
                                                followerCount.as("follower")
                ))
                .from(follow)
                .innerJoin(user).on(follow.following.id.eq(user.id))
                .groupBy(user.id, user.email, user.nickname, user.intro)
                .orderBy(followerCount.desc())
                .offset((long) page * size)
                .limit(size)
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(follow)
                .innerJoin(user).on(follow.following.id.eq(user.id))
                .groupBy(user.id)
                .fetch().size();

        return new PageImpl<>(results, PageRequest.of(page, size), total);
    }




}
