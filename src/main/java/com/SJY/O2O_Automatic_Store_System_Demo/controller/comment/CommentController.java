package com.SJY.O2O_Automatic_Store_System_Demo.controller.comment;

import com.SJY.O2O_Automatic_Store_System_Demo.aop.AssignMemberId;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.comment.CommentCreateRequest;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.comment.CommentReadCondition;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.response.Response;
import com.SJY.O2O_Automatic_Store_System_Demo.service.comment.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "댓글 목록 조회", description = "댓글 목록을 조회한다.")
    @GetMapping("/api/comments")
    public ResponseEntity<Response> readAll(@Parameter(description = "댓글 조회 조건") @Valid @ModelAttribute CommentReadCondition cond) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.success(commentService.readAll(cond)));
    }

    @Operation(summary = "댓글 생성", description = "댓글을 생성한다.")
    @AssignMemberId
    @PostMapping("/api/comments")
    public ResponseEntity<Response> create(@Parameter(description = "댓글 생성 요청") @Valid @RequestBody CommentCreateRequest req) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Response.success(commentService.create(req)));
    }

    @Operation(summary = "댓글 삭제", description = "댓글을 삭제한다.")
    @DeleteMapping("/api/comments/{id}")
    public ResponseEntity<Response> delete(@Parameter(description = "댓글 id") @PathVariable(name = "id")Long id) {
        commentService.delete(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.success());
    }
}