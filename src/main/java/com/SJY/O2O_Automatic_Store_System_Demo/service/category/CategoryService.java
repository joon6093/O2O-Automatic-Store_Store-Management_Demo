package com.SJY.O2O_Automatic_Store_System_Demo.service.category;

import com.SJY.O2O_Automatic_Store_System_Demo.dto.category.CategoryCreateRequest;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.category.CategoryCreateResponse;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.category.CategoryDto;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.category.Category;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.CategoryNotFoundException;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<CategoryDto> readAll() {
        List<Category> topLevelCategories = categoryRepository.findTopLevelCategories();
        return topLevelCategories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CategoryDto convertToDto(Category category) {
        List<CategoryDto> childDtos = category.getChildren().isEmpty()
                ? new ArrayList<>()
                : category.getChildren().stream().map(this::convertToDto).collect(Collectors.toList());

        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .children(childDtos)
                .build();
    }

    @Transactional
    public CategoryCreateResponse create(CategoryCreateRequest req) {
        Category category = Category.builder()
                            .name(req.getName())
                            .build();
        if (req.getParentId() != null) {
            Category parent  = categoryRepository.findById(req.getParentId())
                    .orElseThrow(CategoryNotFoundException::new);
            parent.addChildCategory(category);
        }
        categoryRepository.save(category);
        return new CategoryCreateResponse(category.getId());
    }

    @Transactional
    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(CategoryNotFoundException::new);

        if (category.getParent() != null) {
            category .getParent().removeChildCategory(category);
        }
        categoryRepository.delete(category);
    }
}