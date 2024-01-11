package com.SJY.O2O_Automatic_Store_System_Demo.dto.message;

import com.SJY.O2O_Automatic_Store_System_Demo.dto.member.MemberDto;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.message.Message;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MessageDto {
    private Long id;
    private String content;
    private MemberDto sender;
    private MemberDto receiver;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    public static MessageDto toDto(Message message) {
        return new MessageDto(
                message.getId(),
                message.getContent(),
                MemberDto.toDto(message.getSender()),
                MemberDto.toDto(message.getReceiver()),
                message.getCreatedAt());
    }
}