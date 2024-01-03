package com.SJY.O2O_Automatic_Store_System_Demo.dto.category;

import com.SJY.O2O_Automatic_Store_System_Demo.entity.category.Category;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.CategoryNotFoundException;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.category.CategoryRepository;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

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

    public static Category toEntity(CategoryCreateRequest req, CategoryRepository categoryRepository) {
        Category parent = Optional.ofNullable(req.getParentId())
                .map(id -> categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new))
                .orElse(null);
        return new Category(req.getName(), parent);
    }
}
