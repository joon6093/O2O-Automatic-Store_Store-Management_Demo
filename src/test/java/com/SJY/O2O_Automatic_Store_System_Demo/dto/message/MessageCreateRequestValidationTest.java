package com.SJY.O2O_Automatic_Store_System_Demo.dto.message;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import java.util.Set;

import static com.SJY.O2O_Automatic_Store_System_Demo.factory.dto.MessageCreateRequestFactory.createMessageCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static java.util.stream.Collectors.toSet;

class MessageCreateRequestValidationTest {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validateTest() {
        // given
        MessageCreateRequest req = createMessageCreateRequest("content", null, 2L);

        // when
        Set<ConstraintViolation<MessageCreateRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isEmpty();
    }

    @Test
    void invalidateByEmptyContentTest() {
        // given
        String invalidValue = null;
        MessageCreateRequest req = createMessageCreateRequest(invalidValue, null, 2L);

        // when
        Set<ConstraintViolation<MessageCreateRequest>> validate = validator.validate(req);

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
        String invalidValue = "  ";
        MessageCreateRequest req = createMessageCreateRequest(invalidValue, null, 2L);

        // when
        Set<ConstraintViolation<MessageCreateRequest>> validate = validator.validate(req);

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
    void invalidateByNotNullMemberIdTest() {
        // given
        Long invalidValue = 1L;
        MessageCreateRequest req = createMessageCreateRequest("content", invalidValue, 2L);

        // when
        Set<ConstraintViolation<MessageCreateRequest>> validate = validator.validate(req);

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
    void invalidateByNullReceiverIdTest() {
        // given
        Long invalidValue = null;
        MessageCreateRequest req = createMessageCreateRequest("content", null, invalidValue);

        // when
        Set<ConstraintViolation<MessageCreateRequest>> validate = validator.validate(req);

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
    void invalidateByNegativeOrZeroReceiverIdTest() {
        // given
        Long invalidValue = 0L;
        MessageCreateRequest req = createMessageCreateRequest("content", 1L, invalidValue);

        // when
        Set<ConstraintViolation<MessageCreateRequest>> validate = validator.validate(req);

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