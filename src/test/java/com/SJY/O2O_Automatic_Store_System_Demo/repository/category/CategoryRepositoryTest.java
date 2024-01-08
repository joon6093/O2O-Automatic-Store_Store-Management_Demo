package com.SJY.O2O_Automatic_Store_System_Demo.repository.category;

import com.SJY.O2O_Automatic_Store_System_Demo.config.QuerydslConfig;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.category.Category;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.CategoryNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static com.SJY.O2O_Automatic_Store_System_Demo.factory.entity.CategoryFactory.createCategory;
import static com.SJY.O2O_Automatic_Store_System_Demo.factory.entity.CategoryFactory.createCategoryWithName;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import(QuerydslConfig.class)
class CategoryRepositoryTest {
    @Autowired
    CategoryRepository categoryRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    void createAndReadTest() {
        // given
        Category category = createCategory();

        // when
        Category savedCategory = categoryRepository.save(category);
        clear();

        // then
        Category foundCategory = categoryRepository.findById(savedCategory.getId()).orElseThrow(CategoryNotFoundException::new);
        assertThat(foundCategory.getId()).isEqualTo(savedCategory.getId());
    }

    @Test
    void readAllTest() {
        // given
        List<Category> categories = List.of("name1", "name2", "name3").stream().map(n -> createCategoryWithName(n)).collect(toList());
        categoryRepository.saveAll(categories);
        clear();

        // when
        List<Category> foundCategories = categoryRepository.findAll();

        // then
        assertThat(foundCategories.size()).isEqualTo(3);
    }

    @Test
    void deleteTest() {
        // given
        Category category = categoryRepository.save(createCategory());
        clear();

        // when
        categoryRepository.delete(category);
        clear();

        // then
        assertThatThrownBy(() -> categoryRepository.findById(category.getId()).orElseThrow(CategoryNotFoundException::new))
                .isInstanceOf(CategoryNotFoundException.class);
    }

    @Test
    void deleteCascadeTest() {
        // given
        Category category1 = categoryRepository.save(createCategoryWithName("category1"));
        Category category2 = categoryRepository.save(createCategory("category2", category1));
        Category category3 = categoryRepository.save(createCategory("category3", category2));
        Category category4 = categoryRepository.save(createCategoryWithName("category4"));
        clear();

        // when
        categoryRepository.deleteById(category1.getId());
        clear();

        // then
        List<Category> result = categoryRepository.findAll();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getId()).isEqualTo(category4.getId());
    }

    @Test
    void findByParentIsNullTest() {
        // given
        Category rootCategory1 = categoryRepository.save(createCategoryWithName("Root Category 1"));
        Category childCategory = categoryRepository.save(createCategory("Child Category", rootCategory1));
        Category rootCategory2 = categoryRepository.save(createCategoryWithName("Root Category 2"));

        // when
        List<Category> result = categoryRepository.findTopLevelCategories();

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting("name").containsExactlyInAnyOrder("Root Category 1", "Root Category 2");
    }

    void clear() {
        em.flush();
        em.clear();
    }
}