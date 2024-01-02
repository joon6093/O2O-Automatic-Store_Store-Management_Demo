package com.SJY.O2O_Automatic_Store_System_Demo.dto.sign;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "로그인 요청 데이터")
public class SignInRequest {

    @Schema(description = "사용자의 이메일", example = "member@email.com", required = true)
    @Email(message = "이메일 형식을 맞춰주세요.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @Schema(description = "사용자의 비밀번호", example = "123456a!", required = true)
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
}
