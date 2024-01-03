package com.SJY.O2O_Automatic_Store_System_Demo.dto.category;

import com.SJY.O2O_Automatic_Store_System_Demo.entity.category.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private Long id;
    private String name;
    private List<CategoryDto> children;

    public static List<CategoryDto> toDtoList(List<Category> categories) {
        return categories.stream()
                .map(CategoryDto::fromEntity)
                .collect(Collectors.toList());
    }

    private static CategoryDto fromEntity(Category category) {
        List<CategoryDto> childDtos = category.getChildren().isEmpty()
                ? new ArrayList<>()
                : toDtoList(new ArrayList<>(category.getChildren()));
        return new CategoryDto(category.getId(), category.getName(), childDtos);
    }

}