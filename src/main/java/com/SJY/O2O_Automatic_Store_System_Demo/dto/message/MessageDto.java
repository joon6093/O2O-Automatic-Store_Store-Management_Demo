package com.SJY.O2O_Automatic_Store_System_Demo.dto.message;

import com.SJY.O2O_Automatic_Store_System_Demo.dto.member.MemberDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageDto {
    private Long id;
    private String content;
    private MemberDto sender;
    private MemberDto receiver;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @Builder
    public MessageDto(Long id, String content, MemberDto sender, MemberDto receiver, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
        this.createdAt = createdAt;
    }
}