package com.SJY.O2O_Automatic_Store_System_Demo.controller.sign;

import com.SJY.O2O_Automatic_Store_System_Demo.dto.sign.SignInRequest;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.sign.SignUpRequest;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.*;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.advice.ExceptionAdvice;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.response.ResponseHandler;
import com.SJY.O2O_Automatic_Store_System_Demo.service.sign.SignService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.SJY.O2O_Automatic_Store_System_Demo.factory.dto.SignInRequestFactory.createSignInRequest;
import static com.SJY.O2O_Automatic_Store_System_Demo.factory.dto.SignUpRequestFactory.createSignUpRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SignControllerAdviceTest {
    @InjectMocks
    SignController signController;
    @Mock
    SignService signService;
    @Mock
    ResponseHandler responseHandler;
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(signController).setControllerAdvice(new ExceptionAdvice(responseHandler)).build();
    }

    @Test
    void signInLoginFailureExceptionTest() throws Exception {
        // given
        SignInRequest req = createSignInRequest("email@email.com", "123456a!");
        given(signService.signIn(any())).willThrow(LoginFailureException.class);

        // when, then
        mockMvc.perform(
                        post("/api/sign-in")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void signInMethodArgumentNotValidExceptionTest() throws Exception {
        // given
        SignInRequest req =createSignInRequest("email", "1234567");

        // when, then
        mockMvc.perform(
                        post("/api/sign-in")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void signUpMemberEmailAlreadyExistsExceptionTest() throws Exception {
        // given
        SignUpRequest req = createSignUpRequest("email@email.com", "123456a!", "username", "nickname");
        doThrow(MemberEmailAlreadyExistsException.class).when(signService).signUp(any());

        // when, then
        mockMvc.perform(
                        post("/api/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isConflict());
    }

    @Test
    void signUpMemberNicknameAlreadyExistsExceptionTest() throws Exception {
        // given
        SignUpRequest req = createSignUpRequest("email@email.com", "123456a!", "username", "nickname");
        doThrow(MemberNicknameAlreadyExistsException.class).when(signService).signUp(any());

        // when, then
        mockMvc.perform(
                        post("/api/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isConflict());
    }

    @Test
    void signUpRoleNotFoundExceptionTest() throws Exception {
        // given
        SignUpRequest req = createSignUpRequest("email@email.com", "123456a!", "username", "nickname");
        doThrow(RoleNotFoundException.class).when(signService).signUp(any());

        // when, then
        mockMvc.perform(
                        post("/api/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound());
    }

    @Test
    void signUpMethodArgumentNotValidExceptionTest() throws Exception {
        // given
        SignUpRequest req = createSignUpRequest("", "", "", "");

        // when, then
        mockMvc.perform(
                        post("/api/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void refreshTokenAuthenticationEntryPointException() throws Exception {
        // given
        given(signService.refreshToken(anyString())).willThrow(RefreshTokenFailureException.class);

        // when, then
        mockMvc.perform(
                        post("/api/refresh-token")
                                .header("Authorization", "refreshToken"))
                .andExpect(status().isNotFound());
    }

    @Test
    void refreshTokenMissingRequestHeaderException() throws Exception {
        // given, when, then
        mockMvc.perform(
                        post("/api/refresh-token"))
                .andExpect(status().isBadRequest());
    }
}