package com.SJY.O2O_Automatic_Store_System_Demo.dto.comment;

import com.SJY.O2O_Automatic_Store_System_Demo.dto.member.MemberDto;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.comment.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private String content;
    private MemberDto member;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    private List<CommentDto> children;

    public static List<CommentDto> toDtoList(List<Comment> comments) {
        return comments.stream()
                .map(CommentDto::fromEntity)
                .collect(Collectors.toList());
    }

    private static CommentDto fromEntity(Comment comment) {
        List<CommentDto> childDtos = comment.getChildren().isEmpty()
                ? new ArrayList<>()
                : toDtoList(new ArrayList<>(comment.getChildren()));

        return comment.isDeleted()
                ? new CommentDto(comment.getId(), null, null, comment.getCreatedAt(), childDtos)
                : new CommentDto(comment.getId(), comment.getContent(), MemberDto.toDto(comment.getMember()), comment.getCreatedAt(), childDtos);
    }
}