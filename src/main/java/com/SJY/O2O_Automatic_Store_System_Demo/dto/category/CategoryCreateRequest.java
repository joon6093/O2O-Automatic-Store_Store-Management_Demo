package com.SJY.O2O_Automatic_Store_System_Demo.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "카테고리 생성 요청")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryCreateRequest {

    @Schema(description = "카테고리 명", example = "my category", required = true)
    @NotBlank(message = "카테고리 명을 입력해주세요.")
    @Size(min = 2, max = 30, message = "카테고리 명은 최소 2글자에서 30글자 입니다.")
    private String name;

    @Schema(description = "부모 카테고리 아이디", example = "7")
    private Long parentId;
}
