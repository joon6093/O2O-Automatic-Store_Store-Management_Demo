package com.SJY.O2O_Automatic_Store_System_Demo.controller.member;

import com.SJY.O2O_Automatic_Store_System_Demo.advice.ExceptionAdvice;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.MemberNotFoundException;
import com.SJY.O2O_Automatic_Store_System_Demo.service.member.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@ExtendWith(MockitoExtension.class)
public class MemberControllerAdviceTest {
    @InjectMocks
    MemberController memberController;
    @Mock
    MemberService memberService;
    MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("i18n/exception");
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).setControllerAdvice(new ExceptionAdvice(messageSource)).build();
    }

    @Test
    void readMemberNotFoundExceptionTest() throws Exception {
        // given
        given(memberService.read(anyLong())).willThrow(MemberNotFoundException.class);

        // when, then
        mockMvc.perform(
                        get("/api/members/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(-1007));
    }

    @Test
    void deleteMemberNotFoundExceptionTest() throws Exception{
        // given
        doThrow(MemberNotFoundException.class).when(memberService).delete(anyLong());

        // when, then
        mockMvc.perform(
                        delete("/api/members/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(-1007));
    }

}