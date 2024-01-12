package com.SJY.O2O_Automatic_Store_System_Demo.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "댓글 생성 요청")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateRequest {

    @Schema(description = "댓글 내용", example = "my comment", required = true)
    @NotBlank(message = "{commentCreateRequest.content.notBlank}")
    private String content;

    @Schema(description = "게시글 아이디", example = "7", required = true)
    @NotNull(message = "{commentCreateRequest.postId.notNull}")
    @Positive(message = "{commentCreateRequest.postId.positive}")
    private Long postId;

    @Schema(hidden = true)
    @Null
    private Long memberId;

    @Schema(description = "부모 댓글 아이디", example = "7")
    private Long parentId;
}