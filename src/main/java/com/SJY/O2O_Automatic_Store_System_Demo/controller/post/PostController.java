package com.SJY.O2O_Automatic_Store_System_Demo.controller.post;

import com.SJY.O2O_Automatic_Store_System_Demo.aop.AssignMemberId;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.post.PostCreateRequest;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.post.PostReadCondition;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.post.PostUpdateRequest;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.response.Response;
import com.SJY.O2O_Automatic_Store_System_Demo.service.post.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "게시글 조회", description = "게시글을 조회한다.")
    @GetMapping("/api/posts/{id}")
    public ResponseEntity<Response> read(@Parameter(description = "게시글 id") @PathVariable(name = "id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.success(postService.read(id)));
    }

    @Operation(summary = "게시글 삭제", description = "게시글을 삭제한다.")
    @DeleteMapping("/api/posts/{id}")
    public ResponseEntity<Response> delete(@Parameter(description = "게시글 id") @PathVariable(name = "id") Long id) {
        postService.delete(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.success());
    }

    @Operation(summary = "게시글 수정", description = "게시글을 수정한다.")
    @PutMapping("/api/posts/{id}")
    public ResponseEntity<Response> update(
            @Parameter(description = "게시글 id", required = true) @PathVariable(name = "id") Long id,
            @Valid @ModelAttribute PostUpdateRequest req) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.success(postService.update(id, req)));
    }

    @Operation(summary = "게시글 목록 조회", description = "게시글 목록을 조회한다.")
    @GetMapping("/api/posts")
    public ResponseEntity<Response> readAll(@Valid PostReadCondition cond) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.success(postService.readAll(cond)));
    }
}
