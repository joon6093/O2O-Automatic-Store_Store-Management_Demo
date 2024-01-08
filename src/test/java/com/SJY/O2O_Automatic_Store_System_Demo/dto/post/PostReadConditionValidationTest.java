package com.SJY.O2O_Automatic_Store_System_Demo.dto.post;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import java.util.Set;

import static com.SJY.O2O_Automatic_Store_System_Demo.factory.dto.PostReadConditionFactory.createPostReadCondition;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

class PostReadConditionValidationTest {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validateTest() {
        // given
        PostReadCondition cond = createPostReadCondition(1, 1);

        // when
        Set<ConstraintViolation<PostReadCondition>> validate = validator.validate(cond);

        // then
        assertThat(validate).isEmpty();
    }

    @Test
    void invalidateByNullPageTest() {
        // given
        Integer invalidValue = null;
        PostReadCondition req = createPostReadCondition(invalidValue, 1);

        // when
        Set<ConstraintViolation<PostReadCondition>> validate = validator.validate(req);

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
    void invalidateByNegativePageTest() {
        // given
        Integer invalidValue = -1;
        PostReadCondition req = createPostReadCondition(invalidValue, 1);

        // when
        Set<ConstraintViolation<PostReadCondition>> validate = validator.validate(req);

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
    void invalidateByNullSizeTest() {
        // given
        Integer invalidValue = null;
        PostReadCondition req = createPostReadCondition(1, invalidValue);

        // when
        Set<ConstraintViolation<PostReadCondition>> validate = validator.validate(req);

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
    void invalidateByNegativeOrZeroPageTest() {
        // given
        Integer invalidValue = 0;
        PostReadCondition req = createPostReadCondition(1, invalidValue);

        // when
        Set<ConstraintViolation<PostReadCondition>> validate = validator.validate(req);

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