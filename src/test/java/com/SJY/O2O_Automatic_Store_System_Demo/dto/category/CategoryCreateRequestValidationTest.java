package com.SJY.O2O_Automatic_Store_System_Demo.dto.category;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static com.SJY.O2O_Automatic_Store_System_Demo.factory.dto.CategoryCreateRequestFactory.*;
import static org.assertj.core.api.Assertions.assertThat;

class CategoryCreateRequestValidationTest {

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validateTest() {
        // given
        CategoryCreateRequest req = createCategoryCreateRequest();

        // when
        Set<ConstraintViolation<CategoryCreateRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isEmpty();
    }

    @Test
    void invalidateByEmptyNameTest() {
        // given
        String invalidValue = null;
        CategoryCreateRequest req = createCategoryCreateRequestWithName(invalidValue);

        // when
        Set<ConstraintViolation<CategoryCreateRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        validate.forEach(violation -> {
            System.out.println("Violation: " + violation.getMessage());
            System.out.println("Invalid value: " + violation.getInvalidValue());
            System.out.println("Property path: " + violation.getPropertyPath());
        });
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByBlankNameTest() {
        // given
        String invalidValue = " ";
        CategoryCreateRequest req = createCategoryCreateRequestWithName(invalidValue);

        // when
        Set<ConstraintViolation<CategoryCreateRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        validate.forEach(violation -> {
            System.out.println("Violation: " + violation.getMessage());
            System.out.println("Invalid value: " + violation.getInvalidValue());
            System.out.println("Property path: " + violation.getPropertyPath());
        });
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByShortNameTest() {
        // given
        String invalidValue = "c";
        CategoryCreateRequest req = createCategoryCreateRequestWithName(invalidValue);

        // when
        Set<ConstraintViolation<CategoryCreateRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        validate.forEach(violation -> {
            System.out.println("Violation: " + violation.getMessage());
            System.out.println("Invalid value: " + violation.getInvalidValue());
            System.out.println("Property path: " + violation.getPropertyPath());
        });
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByLongNameTest() {
        // given
        String invalidValue = "c".repeat(50);

        CategoryCreateRequest req = createCategoryCreateRequestWithName(invalidValue);

        // when
        Set<ConstraintViolation<CategoryCreateRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        validate.forEach(violation -> {
            System.out.println("Violation: " + violation.getMessage());
            System.out.println("Invalid value: " + violation.getInvalidValue());
            System.out.println("Property path: " + violation.getPropertyPath());
        });
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }
}