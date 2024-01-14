package com.SJY.O2O_Automatic_Store_System_Demo.controller.message;

import com.SJY.O2O_Automatic_Store_System_Demo.aop.AssignMemberId;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.message.MessageCreateRequest;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.message.MessageReadCondition;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.response.Response;
import com.SJY.O2O_Automatic_Store_System_Demo.service.message.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @Operation(summary = "송신자의 쪽지 목록 조회", description = "송신자의 쪽지 목록을 조회한다.")
    @AssignMemberId
    @GetMapping("/api/messages/sender")
    public ResponseEntity<Response> readAllBySender(@Parameter(description = "쪽지 조회 조건") @Valid @ModelAttribute MessageReadCondition cond) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.success(messageService.readAllBySender(cond)));
    }

    @Operation(summary = "수신자의 쪽지 목록 조회", description = "수신자의 쪽지 목록을 조회한다.")
    @AssignMemberId
    @GetMapping("/api/messages/receiver")
    public ResponseEntity<Response> readAllByReceiver(@Parameter(description = "쪽지 조회 조건") @Valid @ModelAttribute MessageReadCondition cond) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.success(messageService.readAllByReceiver(cond)));
    }

    @Operation(summary = "쪽지 조회", description = "쪽지를 조회한다.")
    @GetMapping("/api/messages/{id}")
    public ResponseEntity<Response> read(@Parameter(description = "쪽지 id") @PathVariable(name = "id")Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.success(messageService.read(id)));
    }

    @Operation(summary = "쪽지 생성", description = "쪽지를 생성한다.")
    @AssignMemberId
    @PostMapping("/api/messages")
    public ResponseEntity<Response> create(@Parameter(description = "쪽지 생성 요청") @Valid @RequestBody MessageCreateRequest req) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Response.success(messageService.create(req)));
    }

    @Operation(summary = "송신자의 쪽지 삭제", description = "송신자의 쪽지를 삭제한다.")
    @DeleteMapping("/api/messages/sender/{id}")
    public ResponseEntity<Response> deleteBySender(@Parameter(description = "쪽지 id") @PathVariable(name = "id")Long id) {
        messageService.deleteBySender(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.success());
    }

    @Operation(summary = "수신자의 쪽지 삭제", description = "수신자의 쪽지를 삭제한다.")
    @DeleteMapping("/api/messages/receiver/{id}")
    public ResponseEntity<Response> deleteByReceiver(@Parameter(description = "쪽지 id") @PathVariable(name = "id")Long id) {
        messageService.deleteByReceiver(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.success());
    }
}