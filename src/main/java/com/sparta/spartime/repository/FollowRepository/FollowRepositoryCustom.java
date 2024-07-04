package com.sparta.spartime.repository.FollowRepository;

import com.sparta.spartime.dto.response.UserTenResponseDto;
import com.sparta.spartime.entity.Post;
import com.sparta.spartime.searchCond.PostSearchCond;
import org.springframework.data.domain.Page;

public interface FollowRepositoryCustom {
    Page<Post> getPostsFollowByUser(PostSearchCond searchCond, int page, int size, int asc);
    Page<UserTenResponseDto> getTopFollowTen();
}
