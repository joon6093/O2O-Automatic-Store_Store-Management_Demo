package com.SJY.O2O_Automatic_Store_System_Demo.controller.post;

import com.SJY.O2O_Automatic_Store_System_Demo.aop.AssignMemberId;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.post.PostCreateRequest;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.response.Response;
import com.SJY.O2O_Automatic_Store_System_Demo.service.post.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Post", description = "게시글 관련 API")
@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;

    @Operation(summary = "게시글 생성", description = "게시글을 생성한다.")
    @AssignMemberId
    @PostMapping("/api/posts")
    public ResponseEntity<Response> create(@Valid @ModelAttribute PostCreateRequest req) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Response.success(postService.create(req)));
    }

}
