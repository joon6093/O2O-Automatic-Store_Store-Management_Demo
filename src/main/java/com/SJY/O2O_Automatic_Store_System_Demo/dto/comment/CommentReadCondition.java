package com.SJY.O2O_Automatic_Store_System_Demo.dto.comment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentReadCondition {
    @NotNull(message = "게시글 번호를 입력해주세요.")
    @PositiveOrZero(message = "올바른 게시글 번호를 입력해주세요. (0 이상)")
    private Long postId;
}