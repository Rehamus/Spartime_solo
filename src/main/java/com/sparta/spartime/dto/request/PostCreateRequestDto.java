package com.sparta.spartime.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCreateRequestDto {
    @NotEmpty
    private String title;
    @NotEmpty
    private String contents;
}
