package com.SJY.O2O_Automatic_Store_System_Demo.exception.response;

import com.SJY.O2O_Automatic_Store_System_Demo.dto.response.Failure;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ResourceBundleMessageSource;

import static com.SJY.O2O_Automatic_Store_System_Demo.exception.type.ExceptionType.BIND_EXCEPTION;
import static com.SJY.O2O_Automatic_Store_System_Demo.exception.type.ExceptionType.EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;

class ResponseHandlerTest {
    ResponseHandler responseHandler;

    @BeforeEach
    void beforeEach() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("i18n/exception");
        responseHandler = new ResponseHandler(messageSource);
    }

    @Test
    void getFailureResponseNoArgsTest() {
        // given, when
        Response failureResponse = responseHandler.getFailureResponse(EXCEPTION);

        // then
        assertThat(failureResponse.getCode()).isEqualTo(-1000);
    }

    @Test
    void getFailureResponseWithArgsTest() {
        // given, when
        Response failureResponse = responseHandler.getFailureResponse(BIND_EXCEPTION, "my args");

        // then
        assertThat(failureResponse.getCode()).isEqualTo(-1003);
        assertThat(((Failure) failureResponse.getResult()).getMsg()).isEqualTo("my args");
    }
}