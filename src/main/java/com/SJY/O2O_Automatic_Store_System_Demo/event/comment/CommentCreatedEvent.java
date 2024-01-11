package com.SJY.O2O_Automatic_Store_System_Demo.event.comment;

import com.SJY.O2O_Automatic_Store_System_Demo.dto.member.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentCreatedEvent {
    private MemberDto publisher;
    private MemberDto postWriter;
    private MemberDto parentWriter;
    private String content;
}