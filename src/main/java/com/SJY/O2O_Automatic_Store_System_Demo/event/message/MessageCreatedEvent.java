package com.SJY.O2O_Automatic_Store_System_Demo.event.message;

import com.SJY.O2O_Automatic_Store_System_Demo.dto.member.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageCreatedEvent {
    private MemberDto publisher;
    private MemberDto receiver;
    private String content;
}
