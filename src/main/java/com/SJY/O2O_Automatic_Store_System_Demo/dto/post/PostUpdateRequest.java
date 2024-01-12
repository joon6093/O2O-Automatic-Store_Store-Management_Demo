package com.SJY.O2O_Automatic_Store_System_Demo.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "게시글 수정 요청")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostUpdateRequest {
    @Schema(description = "게시글 제목", example = "my title", required = true)
    @NotBlank(message = "{postUpdateRequest.title.notBlank}")
    private String title;

    @Schema(description = "게시글 본문", example = "my content", required = true)
    @NotBlank(message = "{postUpdateRequest.content.notBlank}")
    private String content;

    @Schema(description = "가격", example = "50000", required = true)
    @NotNull(message = "{postUpdateRequest.price.notNull}")
    @PositiveOrZero(message = "{postUpdateRequest.price.positiveOrZero}")
    private Long price;

    @Schema(description = "추가된 이미지")
    private List<MultipartFile> addedImages = new ArrayList<>();

    @Schema(description = "제거된 이미지 아이디")
    private List<Long> deletedImages = new ArrayList<>();
}