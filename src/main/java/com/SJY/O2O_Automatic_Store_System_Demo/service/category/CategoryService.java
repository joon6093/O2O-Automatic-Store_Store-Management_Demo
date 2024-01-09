package com.SJY.O2O_Automatic_Store_System_Demo.service.category;

import com.SJY.O2O_Automatic_Store_System_Demo.dto.category.CategoryCreateRequest;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.category.CategoryDto;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.category.Category;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.CategoryNotFoundException;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<CategoryDto> readAll() {
        List<Category> rootCategories = categoryRepository.findTopLevelCategories();
        return CategoryDto.toDtoList(rootCategories);
    }

    @Transactional
    public void create(CategoryCreateRequest req) {
        Category category = new Category(req.getName());
        if (req.getParentId() != null) {
            Category parent  = categoryRepository.findById(req.getParentId())
                    .orElseThrow(CategoryNotFoundException::new);
            parent.addChildCategory(category);
        }
        categoryRepository.save(category);
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