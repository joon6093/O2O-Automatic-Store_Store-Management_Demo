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
        List<Category> rootCategories = categoryRepository.findByParentIsNull();
        return CategoryDto.toDtoList(rootCategories);
    }

    @Transactional
    public void create(CategoryCreateRequest req) {
        Category parent = null;
        if (req.getParentId() != null) {
            parent = categoryRepository.findById(req.getParentId())
                    .orElseThrow(CategoryNotFoundException::new);
        }
        Category newCategory = new Category(req.getName(), parent);
        if (parent != null) {
            parent.addChildCategory(newCategory);
        }
        else{
            categoryRepository.save(newCategory);
        }
    }

    @Transactional
    public void delete(Long id) {
        Category categoryToDelete = categoryRepository.findById(id)
                .orElseThrow(CategoryNotFoundException::new);

        if (categoryToDelete.getParent() != null) {
            categoryToDelete .getParent().removeChildCategory(categoryToDelete);
        }
        else {
            categoryRepository.delete(categoryToDelete);
        }
    }
}