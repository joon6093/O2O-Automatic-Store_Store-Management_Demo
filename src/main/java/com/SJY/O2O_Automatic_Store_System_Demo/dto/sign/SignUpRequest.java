package com.SJY.O2O_Automatic_Store_System_Demo.dto.sign;

import com.SJY.O2O_Automatic_Store_System_Demo.entity.member.Member;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.member.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "회원가입 요청 데이터")
public class SignUpRequest {

    @Schema(description = "이메일", example = "member@email.com", required = true)
    @Email(message = "{signUpRequest.email.email}")
    @NotBlank(message = "{signUpRequest.email.notBlank}")
    private String email;

    @Schema(description = "비밀번호 (최소 8자리, 알파벳, 숫자, 특수문자 포함)", example = "123456a!", required = true)
    @NotBlank(message = "{signUpRequest.password.notBlank}")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
             message = "{signUpRequest.password.pattern}")
    private String password;

    @Schema(description = "사용자 이름 (한글 또는 알파벳)", example = "송제용", required = true)
    @NotBlank(message = "{signUpRequest.username.notBlank}")
    @Size(min=2, message = "{signUpRequest.username.size}")
    @Pattern(regexp = "^[A-Za-z가-힣]+$", message = "{signUpRequest.username.pattern}")
    private String username;

    @Schema(description = "닉네임 (한글 또는 알파벳)", example = "달려라러너왕", required = true)
    @NotBlank(message = "{signUpRequest.nickname.notBlank}")
    @Size(min=2, message = "{signUpRequest.nickname.size}")
    @Pattern(regexp = "^[A-Za-z가-힣]+$", message = "{signUpRequest.nickname.pattern}")
    private String nickname;

    public static Member toEntity(SignUpRequest req, Role role, PasswordEncoder encoder) {
        return new Member(req.email, encoder.encode(req.password), req.username, req.nickname, List.of(role));
    }
}