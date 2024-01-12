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
    @NotNull(message = "{commentReadCondition.postId.notNull}")
    @PositiveOrZero(message = "{commentReadCondition.postId.positiveOrZero}")
    private Long postId;
}