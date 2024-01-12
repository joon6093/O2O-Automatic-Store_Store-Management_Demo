package com.SJY.O2O_Automatic_Store_System_Demo.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Schema(description = "게시글 생성 요청")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateRequest {

    @Schema(description = "게시글 제목", example = "my title", required = true)
    @NotBlank(message = "{postCreateRequest.title.notBlank}")
    private String title;

    @Schema(description = "게시글 본문", example = "my content", required = true)
    @NotBlank(message = "{postCreateRequest.content.notBlank}")
    private String content;

    @Schema(description = "가격", example = "50000", required = true)
    @NotNull(message = "{postCreateRequest.price.notNull")
    @PositiveOrZero(message = "{postCreateRequest.price.positiveOrZero}")
    private Long price;

    @Schema(hidden = true)
    @Null
    private Long memberId;

    @Schema(description = "카테고리 아이디", example = "3", required = true)
    @NotNull(message = "{postCreateRequest.categoryId.notNull}")
    @PositiveOrZero(message = "{postCreateRequest.categoryId.positiveOrZero}")
    private Long categoryId;

    @Schema(description = "이미지", type = "array", format = "binary")
    private List<MultipartFile> images = new ArrayList<>();
}