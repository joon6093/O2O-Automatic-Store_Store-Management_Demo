package com.SJY.O2O_Automatic_Store_System_Demo.controller.category;

import com.SJY.O2O_Automatic_Store_System_Demo.dto.category.CategoryCreateRequest;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.response.Response;
import com.SJY.O2O_Automatic_Store_System_Demo.service.category.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "모든 카테고리 조회", description = "모든 카테고리를 조회한다.")
    @GetMapping("/api/categories")
    public ResponseEntity<Response> readAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.success(categoryService.readAll()));
    }

    @Operation(summary = "카테고리 생성", description = "카테고리를 생성한다.")
    @PostMapping("/api/categories")
    public ResponseEntity<Response> create(@Parameter(description = "카테고리 생성 요청") @Valid @RequestBody CategoryCreateRequest req) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Response.success(categoryService.create(req)));
    }

    @Operation(summary = "카테고리 삭제", description = "카테고리를 삭제한다.")
    @DeleteMapping("/api/categories/{id}")
    public ResponseEntity<Response> delete(@Parameter(description = "카테고리 id") @PathVariable(name = "id") Long id) {
        categoryService.delete(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.success());
    }
}
