package com.SJY.O2O_Automatic_Store_System_Demo.dto.post;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import static com.SJY.O2O_Automatic_Store_System_Demo.factory.dto.PostUpdateRequestFactory.createPostUpdateRequest;
import static java.util.stream.Collectors.toSet;

class PostUpdateRequestValidationTest {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validateTest() {
        // given
        PostUpdateRequest req = createPostUpdateRequest("title", "content", 1234L, List.of(), List.of());

        // when
        Set<ConstraintViolation<PostUpdateRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isEmpty();
    }

    @Test
    void invalidateByEmptyTitleTest() {
        // given
        String invalidValue = null;
        PostUpdateRequest req = createPostUpdateRequest(invalidValue, "content", 1234L, List.of(), List.of());

        // when
        Set<ConstraintViolation<PostUpdateRequest>> validate = validator.validate(req);

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
    void invalidateByBlankTitleTest() {
        // given
        String invalidValue = " ";
        PostUpdateRequest req = createPostUpdateRequest(invalidValue, "content", 1234L, List.of(), List.of());

        // when
        Set<ConstraintViolation<PostUpdateRequest>> validate = validator.validate(req);

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
    void invalidateByEmptyContentTest() {
        // given
        String invalidValue = null;
        PostUpdateRequest req = createPostUpdateRequest("title", invalidValue, 1234L, List.of(), List.of());

        // when
        Set<ConstraintViolation<PostUpdateRequest>> validate = validator.validate(req);

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
    void invalidateByBlankContentTest() {
        // given
        String invalidValue = " ";
        PostUpdateRequest req = createPostUpdateRequest("title", invalidValue, 1234L, List.of(), List.of());

        // when
        Set<ConstraintViolation<PostUpdateRequest>> validate = validator.validate(req);

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
    void invalidateByNullPriceTest() {
        // given
        Long invalidValue = null;
        PostUpdateRequest req = createPostUpdateRequest("title", "content", invalidValue, List.of(), List.of());

        // when
        Set<ConstraintViolation<PostUpdateRequest>> validate = validator.validate(req);

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
    void invalidateByNegativePriceTest() {
        // given
        Long invalidValue = -1L;
        PostUpdateRequest req = createPostUpdateRequest("title", "content", invalidValue, List.of(), List.of());

        // when
        Set<ConstraintViolation<PostUpdateRequest>> validate = validator.validate(req);

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