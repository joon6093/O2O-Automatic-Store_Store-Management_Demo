package com.SJY.O2O_Automatic_Store_System_Demo.service.category;

import com.SJY.O2O_Automatic_Store_System_Demo.dto.category.CategoryCreateRequest;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.category.CategoryDto;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.category.Category;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.CategoryNotFoundException;
import com.SJY.O2O_Automatic_Store_System_Demo.factory.entity.CategoryFactory;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.category.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;

import static com.SJY.O2O_Automatic_Store_System_Demo.factory.dto.CategoryCreateRequestFactory.createCategoryCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    CategoryService categoryService;
    @Mock
    CategoryRepository categoryRepository;

    @Test
    void readAllTest() {
        // given
        Category parent1 = CategoryFactory.createCategoryWithName("parent1");
        Category child1 = CategoryFactory.createCategory("child1", parent1);
        Category child2 = CategoryFactory.createCategory("child2", parent1);

        Category parent2 = CategoryFactory.createCategoryWithName("parent2");

        List<Category> categories = List.of(parent1, child1, child2, parent2);
        given(categoryRepository.findTopLevelCategories()).willReturn(List.of(parent1, parent2));

        // when
        List<CategoryDto> result = categoryService.readAll();

        // then
        assertThat(result.size()).isEqualTo(2);

        // 검증 - 첫 번째 부모 카테고리와 그 자식 카테고리
        assertThat(result.get(0).getName()).isEqualTo("parent1");
        assertThat(result.get(0).getChildren()).hasSize(2);

        // 검증 - 두 번째 부모 카테고리 (자식이 없음)
        assertThat(result.get(1).getName()).isEqualTo("parent2");
        assertThat(result.get(1).getChildren()).isEmpty();
    }

    @Test
    void createTest() {
        // given
        CategoryCreateRequest req = createCategoryCreateRequest();

        // when
        categoryService.create(req);

        // then
        verify(categoryRepository).save(any());
    }

    @Test
    void deleteTest() {
        // given
        Long categoryId = 1L;
        Category mockCategory = mock(Category.class);
        given(categoryRepository.findById(categoryId)).willReturn(Optional.of(mockCategory));

        // when
        categoryService.delete(categoryId);

        // then
        verify(categoryRepository).findById(categoryId);
        verify(mockCategory, times(1)).getParent();
    }

    @Test
    void deleteExceptionByCategoryNotFoundTest() {
        // given
        Long categoryId = 1L;
        given(categoryRepository.findById(categoryId)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> categoryService.delete(categoryId))
                .isInstanceOf(CategoryNotFoundException.class);
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, never()).delete(any(Category.class));
    }

    
}