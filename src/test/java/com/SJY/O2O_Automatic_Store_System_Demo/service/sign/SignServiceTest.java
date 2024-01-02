package com.SJY.O2O_Automatic_Store_System_Demo.service.sign;

import com.SJY.O2O_Automatic_Store_System_Demo.config.tocken.TokenHelper;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.sign.RefreshTokenResponse;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.sign.SignInRequest;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.sign.SignInResponse;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.sign.SignUpRequest;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.member.Role;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.member.RoleType;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.*;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.member.MemberRepository;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.role.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.SJY.O2O_Automatic_Store_System_Demo.factory.dto.SignUpRequestFactory.createSignUpRequest;
import static com.SJY.O2O_Automatic_Store_System_Demo.factory.entity.MemberFactory.createMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;
@ExtendWith(MockitoExtension.class)
public class SignServiceTest {

    private SignService signService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    @Qualifier("accessTokenHelper")
    private TokenHelper accessTokenHelper;
    @Mock
    @Qualifier("refreshTokenHelper")
    private TokenHelper refreshTokenHelper;
    @BeforeEach
    void beforeEach() {
        signService = new SignService(memberRepository, roleRepository, passwordEncoder, accessTokenHelper, refreshTokenHelper);
    }

    @Test
    void signUpTest() {
        // given
        SignUpRequest req = createSignUpRequest();
        given(roleRepository.findByRoleType(RoleType.ROLE_NORMAL)).willReturn(Optional.of(new Role(RoleType.ROLE_NORMAL)));

        // when
        signService.signUp(req);

        // then
        verify(passwordEncoder).encode(req.getPassword());
        verify(memberRepository).save(any());
    }

    @Test
    void validateSignUpByDuplicateEmailTest() {
        // given
        given(memberRepository.existsByEmail(anyString())).willReturn(true);

        // when, then
        assertThatThrownBy(() -> signService.signUp(createSignUpRequest()))
                .isInstanceOf(MemberEmailAlreadyExistsException.class);
    }

    @Test
    void validateSignUpByDuplicateNicknameTest() {
        // given
        given(memberRepository.existsByNickname(anyString())).willReturn(true);

        // when, then
        assertThatThrownBy(() -> signService.signUp(createSignUpRequest()))
                .isInstanceOf(MemberNicknameAlreadyExistsException.class);
    }

    @Test
    void signUpRoleNotFoundTest() {
        // given
        given(roleRepository.findByRoleType(RoleType.ROLE_NORMAL)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> signService.signUp(createSignUpRequest()))
                .isInstanceOf(RoleNotFoundException.class);
    }

    @Test
    void signInTest() {
        // given
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(createMember()));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
        given(accessTokenHelper.createToken(anyString())).willReturn("access");
        given(refreshTokenHelper.createToken(anyString())).willReturn("refresh");

        // when
        SignInResponse res = signService.signIn(new SignInRequest("email", "password"));

        // then
        assertThat(res.getAccessToken()).isEqualTo("access");
        assertThat(res.getRefreshToken()).isEqualTo("refresh");
    }

    @Test
    void signInExceptionByNoneMemberTest() {
        // given
        given(memberRepository.findByEmail(any())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> signService.signIn(new SignInRequest("email", "password")))
                .isInstanceOf(LoginFailureException.class);
    }

    @Test
    void signInExceptionByInvalidPasswordTest() {
        // given
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(createMember()));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

        // when, then
        assertThatThrownBy(() -> signService.signIn(new SignInRequest("email", "password")))
                .isInstanceOf(LoginFailureException.class);
    }

    @Test
    void refreshTokenTest() {
        // given
        String refreshToken = "refreshToken";
        String subject = "subject";
        String accessToken = "accessToken";
        given(refreshTokenHelper.validate(refreshToken)).willReturn(true);
        given(refreshTokenHelper.extractSubject(refreshToken)).willReturn(subject);
        given(accessTokenHelper.createToken(subject)).willReturn(accessToken);

        // when
        RefreshTokenResponse res = signService.refreshToken(refreshToken);

        // then
        assertThat(res.getAccessToken()).isEqualTo(accessToken);
    }

    @Test
    void refreshTokenExceptionByInvalidTokenTest() {
        // given
        String refreshToken = "refreshToken";
        given(refreshTokenHelper.validate(refreshToken)).willReturn(false);

        // when, then
        assertThatThrownBy(() -> signService.refreshToken(refreshToken))
                .isInstanceOf(AuthenticationEntryPointException.class);
    }
}