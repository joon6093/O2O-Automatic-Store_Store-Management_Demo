package com.SJY.O2O_Automatic_Store_System_Demo.controller.category;

import com.SJY.O2O_Automatic_Store_System_Demo.exception.CategoryNotFoundException;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.advice.ExceptionAdvice;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.response.ResponseHandler;
import com.SJY.O2O_Automatic_Store_System_Demo.service.category.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@ExtendWith(MockitoExtension.class)
class CategoryControllerAdviceTest {
    @InjectMocks
    CategoryController categoryController;
    @Mock
    CategoryService categoryService;
    @Mock
    ResponseHandler responseHandler;
    MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).setControllerAdvice(new ExceptionAdvice(responseHandler)).build();
    }

    @Test
    void deleteTest() throws Exception {
        // given
        doThrow(CategoryNotFoundException.class).when(categoryService).delete(anyLong());

        // when, then
        mockMvc.perform(delete("/api/categories/{id}", 1L))
                .andExpect(status().isNotFound());
    }
}