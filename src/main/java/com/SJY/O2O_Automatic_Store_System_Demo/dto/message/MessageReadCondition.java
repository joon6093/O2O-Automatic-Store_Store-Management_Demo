package com.SJY.O2O_Automatic_Store_System_Demo.dto.message;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageReadCondition {

    @Schema(hidden = true)
    @Null
    private Long memberId;

    @Schema(description = "마지막 쪽지 id", example = "7", required = true)
    private Long lastMessageId = Long.MAX_VALUE;

    @Schema(description = "페이지 크기", example = "10", required = true)
    @NotNull(message = "페이지 크기를 입력해주세요.")
    @Positive(message = "올바른 페이지 크기를 입력해주세요. (1 이상)")
    private Integer size;
}