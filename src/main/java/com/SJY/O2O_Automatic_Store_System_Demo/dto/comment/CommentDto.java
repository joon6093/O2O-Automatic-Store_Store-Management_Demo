package com.SJY.O2O_Automatic_Store_System_Demo.dto.comment;

import com.SJY.O2O_Automatic_Store_System_Demo.dto.member.MemberDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentDto {
    private Long id;
    private String content;
    private MemberDto member;
    private LocalDateTime createdAt;
    private List<CommentDto> children;

    @Builder
    public CommentDto(Long id, String content, MemberDto member, LocalDateTime createdAt, List<CommentDto> children) {
        this.id = id;
        this.content = content;
        this.member = member;
        this.createdAt = createdAt;
        this.children = children;
    }
}