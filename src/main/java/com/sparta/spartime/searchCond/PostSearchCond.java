package com.sparta.spartime.searchCond;

import com.sparta.spartime.entity.Post;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostSearchCond {
    private Long userId;
    private String title;
    private String contents;
    private Post.Type type;
}
