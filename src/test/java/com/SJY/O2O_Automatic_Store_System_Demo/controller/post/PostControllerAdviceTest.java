package com.SJY.O2O_Automatic_Store_System_Demo.controller.post;

import com.SJY.O2O_Automatic_Store_System_Demo.dto.post.PostCreateRequest;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.CategoryNotFoundException;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.MemberNotFoundException;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.PostNotFoundException;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.UnsupportedImageFormatException;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.advice.ExceptionAdvice;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.response.ResponseHandler;
import com.SJY.O2O_Automatic_Store_System_Demo.service.post.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.SJY.O2O_Automatic_Store_System_Demo.factory.dto.PostCreateRequestFactory.createPostCreateRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PostControllerAdviceTest {
    @InjectMocks
    PostController postController;
    @Mock
    PostService postService;
    @Mock
    ResponseHandler responseHandler;
    MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(postController).setControllerAdvice(new ExceptionAdvice(responseHandler)).build();
    }

    @Test
    void createExceptionByMemberNotFoundException() throws Exception{
        // given
        given(postService.create(any())).willThrow(MemberNotFoundException.class);

        // when, then
        performCreate()
                .andExpect(status().isNotFound());
    }

    @Test
    void createExceptionByCategoryNotFoundException() throws Exception{
        // given
        given(postService.create(any())).willThrow(CategoryNotFoundException.class);

        // when, then
        performCreate()
                .andExpect(status().isNotFound());
    }

    @Test
    void createExceptionByUnsupportedImageFormatException() throws Exception{
        // given
        given(postService.create(any())).willThrow(UnsupportedImageFormatException.class);

        // when, then
        performCreate()
                .andExpect(status().isNotFound());
    }

    private ResultActions performCreate() throws Exception {
        PostCreateRequest req = createPostCreateRequest();
        return mockMvc.perform(
                multipart("/api/posts")
                        .param("title", req.getTitle())
                        .param("content", req.getContent())
                        .param("price", String.valueOf(req.getPrice()))
                        .param("categoryId", String.valueOf(req.getCategoryId()))
                        .with(requestPostProcessor -> {
                            requestPostProcessor.setMethod("POST");
                            return requestPostProcessor;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA));
    }

    @Test
    void readExceptionByPostNotFoundTest() throws Exception {
        // given
        given(postService.read(anyLong())).willThrow(PostNotFoundException.class);

        // when, then
        mockMvc.perform(
                        get("/api/posts/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteExceptionByPostNotFoundTest() throws Exception {
        // given
        doThrow(PostNotFoundException.class).when(postService).delete(anyLong());

        // when, then
        mockMvc.perform(
                        delete("/api/posts/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateExceptionByPostNotFoundTest() throws Exception{
        // given
        given(postService.update(anyLong(), any())).willThrow(PostNotFoundException.class);

        // when, then
        mockMvc.perform(
                        multipart("/api/posts/{id}", 1L)
                                .param("title", "title")
                                .param("content", "content")
                                .param("price", "1234")
                                .with(requestPostProcessor -> {
                                    requestPostProcessor.setMethod("PUT");
                                    return requestPostProcessor;
                                })
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isNotFound());
    }
}