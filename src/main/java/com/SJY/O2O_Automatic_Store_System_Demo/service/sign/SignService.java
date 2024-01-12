package com.SJY.O2O_Automatic_Store_System_Demo.service.sign;

import com.SJY.O2O_Automatic_Store_System_Demo.config.tocken.TokenHelper;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.sign.*;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.member.Member;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.member.Role;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.member.RoleType;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.*;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.member.MemberRepository;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.role.RoleRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SignService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenHelper accessTokenHelper;
    private final TokenHelper refreshTokenHelper;

    public SignService(MemberRepository memberRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, @Qualifier("accessTokenHelper") TokenHelper accessTokenHelper, @Qualifier("refreshTokenHelper") TokenHelper refreshTokenHelper) {
        this.memberRepository = memberRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.accessTokenHelper = accessTokenHelper;
        this.refreshTokenHelper = refreshTokenHelper;
    }

    @Transactional
    public SignUpResponse signUp(SignUpRequest req) {
        validateSignUpInfo(req);
        Role role = roleRepository.findByRoleType(RoleType.ROLE_NORMAL).orElseThrow(RoleNotFoundException::new);
        Member member = Member.builder()
                        .email(req.getEmail())
                        .password(passwordEncoder.encode(req.getPassword()))
                        .username(req.getUsername())
                        .nickname(req.getNickname())
                        .roles(List.of(role))
                        .build();
        memberRepository.save(member);
        return new SignUpResponse(member.getId());
    }

    private void validateSignUpInfo(SignUpRequest req) {
        if(memberRepository.existsByEmail(req.getEmail()))
            throw new MemberEmailAlreadyExistsException(req.getEmail());
        if(memberRepository.existsByNickname(req.getNickname()))
            throw new MemberNicknameAlreadyExistsException(req.getNickname());
    }

    @Transactional(readOnly = true)
    public SignInResponse signIn(SignInRequest req) {
        Member member = memberRepository.findWithRolesByEmail(req.getEmail()).orElseThrow(LoginFailureException::new);
        validatePassword(req, member);
        TokenHelper.PrivateClaims privateClaims = createPrivateClaims(member);
        String accessToken = accessTokenHelper.createToken(privateClaims);
        String refreshToken = refreshTokenHelper.createToken(privateClaims);
        return new SignInResponse(accessToken, refreshToken);
    }

    private void validatePassword(SignInRequest req, Member member) {
        if(!passwordEncoder.matches(req.getPassword(), member.getPassword())) {
            throw new LoginFailureException();
        }
    }

    private TokenHelper.PrivateClaims createPrivateClaims(Member member) {
        return new TokenHelper.PrivateClaims(
                String.valueOf(member.getId()),
                member.getRoles().stream()
                        .map(memberRole -> memberRole.getRole())
                        .map(role -> role.getRoleType())
                        .map(roleType -> roleType.toString())
                        .collect(Collectors.toList()));
    }

    public RefreshTokenResponse refreshToken(String rToken) {
        TokenHelper.PrivateClaims privateClaims = refreshTokenHelper.parse(rToken).orElseThrow(RefreshTokenFailureException::new);
        String accessToken = accessTokenHelper.createToken(privateClaims);
        return new RefreshTokenResponse(accessToken);
    }
}