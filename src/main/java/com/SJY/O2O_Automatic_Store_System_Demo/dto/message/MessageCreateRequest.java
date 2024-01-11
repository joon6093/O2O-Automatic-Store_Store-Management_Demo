package com.SJY.O2O_Automatic_Store_System_Demo.dto.message;

import com.SJY.O2O_Automatic_Store_System_Demo.entity.message.Message;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.member.MemberRepository;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.MemberNotFoundException;

@Schema(description = "쪽지 생성 요청")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageCreateRequest {
    @Schema(description = "쪽지 내용", example = "my message", required = true)
    @NotBlank(message = "쪽지를 입력해주세요.")
    private String content;

    @Schema(hidden = true)
    @Null
    private Long memberId;

    @Schema(description = "수신자 아이디", example = "7", required = true)
    @NotNull(message = "수신자 아이디를 입력해주세요.")
    @Positive(message = "올바른 수신자 아이디를 입력해주세요.")
    private Long receiverId;

    public static Message toEntity(MessageCreateRequest req, MemberRepository memberRepository) {
        return new Message(
                req.content,
                memberRepository.findById(req.memberId).orElseThrow(MemberNotFoundException::new),
                memberRepository.findById(req.receiverId).orElseThrow(MemberNotFoundException::new)
        );
    }
}