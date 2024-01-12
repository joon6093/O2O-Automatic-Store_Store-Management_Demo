package com.SJY.O2O_Automatic_Store_System_Demo.dto.message;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "쪽지 생성 요청")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageCreateRequest {
    @Schema(description = "쪽지 내용", example = "my message", required = true)
    @NotBlank(message = "{messageCreateRequest.content.notBlank}")
    private String content;

    @Schema(hidden = true)
    @Null
    private Long memberId;

    @Schema(description = "수신자 아이디", example = "7", required = true)
    @NotNull(message = "{messageCreateRequest.receiverId.notNull}")
    @Positive(message = "{messageCreateRequest.receiverId.positive}")
    private Long receiverId;
}