package com.SJY.O2O_Automatic_Store_System_Demo.dto.post;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostListDto {
    private Long totalElements;
    private Integer totalPages;
    private boolean hasNext;
    private List<PostSimpleDto> postList;

    @Builder
    public PostListDto(Long totalElements, Integer totalPages, boolean hasNext, List<PostSimpleDto> postList) {
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.hasNext = hasNext;
        this.postList = postList;
    }
}