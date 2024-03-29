package com.SJY.O2O_Automatic_Store_System_Demo.dto.sign;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.SJY.O2O_Automatic_Store_System_Demo.factory.dto.SignUpRequestFactory.*;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

class SignUpRequestValidationTest {

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validateTest() {
        // given
        SignUpRequest req = createSignUpRequest();

        // when
        Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isEmpty();
    }

    @Test
    void invalidateByNotFormattedEmailTest() {
        // given
        String invalidValue = "email";
        SignUpRequest req = createSignUpRequestWithEmail(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        validate.forEach(violation -> {
            System.out.println("Violation: " + violation.getMessage());
            System.out.println("Invalid value: " + violation.getInvalidValue());
            System.out.println("Property path: " + violation.getPropertyPath());
        });
        assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByEmptyEmailTest() {
        // given
        String invalidValue = null;
        SignUpRequest req = createSignUpRequestWithEmail(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        validate.forEach(violation -> {
            System.out.println("Violation: " + violation.getMessage());
            System.out.println("Invalid value: " + violation.getInvalidValue());
            System.out.println("Property path: " + violation.getPropertyPath());
        });
        assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByBlankEmailTest() {
        // given
        String invalidValue = " ";
        SignUpRequest req = createSignUpRequestWithEmail(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        validate.forEach(violation -> {
            System.out.println("Violation: " + violation.getMessage());
            System.out.println("Invalid value: " + violation.getInvalidValue());
            System.out.println("Property path: " + violation.getPropertyPath());
        });
        assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByEmptyPasswordTest() {
        // given
        String invalidValue = null;
        SignUpRequest req = createSignUpRequestWithPassword(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        validate.forEach(violation -> {
            System.out.println("Violation: " + violation.getMessage());
            System.out.println("Invalid value: " + violation.getInvalidValue());
            System.out.println("Property path: " + violation.getPropertyPath());
        });
        assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByBlankPasswordTest() {
        // given
        String invalidValue = "        ";
        SignUpRequest req = createSignUpRequestWithPassword(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        validate.forEach(violation -> {
            System.out.println("Violation: " + violation.getMessage());
            System.out.println("Invalid value: " + violation.getInvalidValue());
            System.out.println("Property path: " + violation.getPropertyPath());
        });
        assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByShortPasswordTest() {
        // given
        String invalidValue = "12312a!";
        SignUpRequest req = createSignUpRequestWithPassword(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        validate.forEach(violation -> {
            System.out.println("Violation: " + violation.getMessage());
            System.out.println("Invalid value: " + violation.getInvalidValue());
            System.out.println("Property path: " + violation.getPropertyPath());
        });
        assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByNoneAlphabetPasswordTest() {
        // given
        String invalidValue = "123!@#123";
        SignUpRequest req = createSignUpRequestWithPassword(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        validate.forEach(violation -> {
            System.out.println("Violation: " + violation.getMessage());
            System.out.println("Invalid value: " + violation.getInvalidValue());
            System.out.println("Property path: " + violation.getPropertyPath());
        });
        assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByNoneNumberPasswordTest() {
        // given
        String invalidValue = "abc!@#abc";
        SignUpRequest req = createSignUpRequestWithPassword(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        validate.forEach(violation -> {
            System.out.println("Violation: " + violation.getMessage());
            System.out.println("Invalid value: " + violation.getInvalidValue());
            System.out.println("Property path: " + violation.getPropertyPath());
        });
        assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByNoneSpecialCasePasswordTest() {
        // given
        String invalidValue = "abc123abc";
        SignUpRequest req = createSignUpRequestWithPassword(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        validate.forEach(violation -> {
            System.out.println("Violation: " + violation.getMessage());
            System.out.println("Invalid value: " + violation.getInvalidValue());
            System.out.println("Property path: " + violation.getPropertyPath());
        });
        assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByEmptyUsernameTest() {
        // given
        String invalidValue = null;
        SignUpRequest req = createSignUpRequestWithUsername(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        validate.forEach(violation -> {
            System.out.println("Violation: " + violation.getMessage());
            System.out.println("Invalid value: " + violation.getInvalidValue());
            System.out.println("Property path: " + violation.getPropertyPath());
        });
        assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByBlankUsernameTest() {
        // given
        String invalidValue = " ";
        SignUpRequest req = createSignUpRequestWithUsername(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        validate.forEach(violation -> {
            System.out.println("Violation: " + violation.getMessage());
            System.out.println("Invalid value: " + violation.getInvalidValue());
            System.out.println("Property path: " + violation.getPropertyPath());
        });
        assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByShortUsernameTest() {
        // given
        String invalidValue = "한";
        SignUpRequest req = createSignUpRequestWithUsername(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        validate.forEach(violation -> {
            System.out.println("Violation: " + violation.getMessage());
            System.out.println("Invalid value: " + violation.getInvalidValue());
            System.out.println("Property path: " + violation.getPropertyPath());
        });
        assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByNotAlphabetOrHangeulUsernameTest() {
        // given
        String invalidValue = "송2jae";
        SignUpRequest req = createSignUpRequestWithUsername(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        validate.forEach(violation -> {
            System.out.println("Violation: " + violation.getMessage());
            System.out.println("Invalid value: " + violation.getInvalidValue());
            System.out.println("Property path: " + violation.getPropertyPath());
        });
        assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByEmptyNicknameTest() {
        // given
        String invalidValue = null;
        SignUpRequest req = createSignUpRequestWithNickname(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        validate.forEach(violation -> {
            System.out.println("Violation: " + violation.getMessage());
            System.out.println("Invalid value: " + violation.getInvalidValue());
            System.out.println("Property path: " + violation.getPropertyPath());
        });
        assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByBlankNicknameTest() {
        // given
        String invalidValue = " ";
        SignUpRequest req = createSignUpRequestWithNickname(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        validate.forEach(violation -> {
            System.out.println("Violation: " + violation.getMessage());
            System.out.println("Invalid value: " + violation.getInvalidValue());
            System.out.println("Property path: " + violation.getPropertyPath());
        });
        assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByShortNicknameTest() {
        // given
        String invalidValue = "한";
        SignUpRequest req = createSignUpRequestWithNickname(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        validate.forEach(violation -> {
            System.out.println("Violation: " + violation.getMessage());
            System.out.println("Invalid value: " + violation.getInvalidValue());
            System.out.println("Property path: " + violation.getPropertyPath());
        });
        assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByNotAlphabetOrHangeulNicknameTest() {
        // given
        String invalidValue = "송2jae";
        SignUpRequest req = createSignUpRequestWithNickname(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        validate.forEach(violation -> {
            System.out.println("Violation: " + violation.getMessage());
            System.out.println("Invalid value: " + violation.getInvalidValue());
            System.out.println("Property path: " + violation.getPropertyPath());
        });
        assertThat(validate.stream().map(ConstraintViolation::getInvalidValue).collect(toSet())).contains(invalidValue);
    }
}