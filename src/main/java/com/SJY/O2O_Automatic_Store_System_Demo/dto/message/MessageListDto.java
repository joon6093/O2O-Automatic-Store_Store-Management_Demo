package com.SJY.O2O_Automatic_Store_System_Demo.dto.message;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageListDto {
    private int numberOfElements;
    private boolean hasNext;
    private List<MessageSimpleDto> messageList;

    @Builder
    public MessageListDto(int numberOfElements, boolean hasNext, List<MessageSimpleDto> messageList) {
        this.numberOfElements = numberOfElements;
        this.hasNext = hasNext;
        this.messageList = messageList;
    }

}