package com.SJY.O2O_Automatic_Store_System_Demo.learning;

import com.SJY.O2O_Automatic_Store_System_Demo.dto.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class WebMvcTest {

    @InjectMocks
    TestController testController;
    MockMvc mockMvc;

    @RestController
    public static class TestController {
        @GetMapping("/test/ignore-null-value")
        public Response ignoreNullValueTest() {
            return Response.success();
        }
    }

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(testController).build();
    }

    @Test
    void ignoreNullValueInJsonResponseTest() throws Exception {
        mockMvc.perform(get("/test/ignore-null-value"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true)) // 'success' 필드가 true인지 검증
                .andExpect(jsonPath("$.code").value(0)) // 'code' 필드가 0인지 검증
                .andExpect(jsonPath("$.result").doesNotExist()); // 'result' 필드가 존재하는지 검증
    }
}