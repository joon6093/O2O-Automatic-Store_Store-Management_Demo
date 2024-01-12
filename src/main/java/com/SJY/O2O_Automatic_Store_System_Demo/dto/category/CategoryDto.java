package com.SJY.O2O_Automatic_Store_System_Demo.dto.category;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryDto {
    private Long id;
    private String name;
    private List<CategoryDto> children;

    @Builder
    public CategoryDto(Long id, String name, List<CategoryDto> children) {
        this.id = id;
        this.name = name;
        this.children = children;
    }
}