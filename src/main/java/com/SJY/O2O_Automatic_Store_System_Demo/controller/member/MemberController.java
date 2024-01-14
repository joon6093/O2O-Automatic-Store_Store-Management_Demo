package com.SJY.O2O_Automatic_Store_System_Demo.controller.member;

import com.SJY.O2O_Automatic_Store_System_Demo.dto.response.Response;
import com.SJY.O2O_Automatic_Store_System_Demo.service.member.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "사용자 정보 조회", description = "사용자 정보를 조회한다.")
    @GetMapping("/api/members/{id}")
    public ResponseEntity<Response> read(@Parameter(description = "사용자 id") @PathVariable(name = "id")Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.success(memberService.read(id)));
    }

    @Operation(summary = "사용자 정보 삭제", description = "사용자 정보를 삭제한다.")
    @DeleteMapping("/api/members/{id}")
    public ResponseEntity<Response> delete(@Parameter(description = "사용자 id") @PathVariable(name = "id")Long id) {
        memberService.delete(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.success());
    }
}