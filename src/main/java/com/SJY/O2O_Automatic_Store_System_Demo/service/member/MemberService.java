package com.SJY.O2O_Automatic_Store_System_Demo.service.member;

import com.SJY.O2O_Automatic_Store_System_Demo.dto.member.MemberDto;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.member.Member;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.MemberNotFoundException;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberDto read(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        return MemberDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .username(member.getUsername())
                .nickname(member.getNickname())
                .createdAt(member.getCreatedAt())
                .build();
    }

    @PreAuthorize("@memberGuard.check(#id)")
    @Transactional
    public void delete(@Param("id")Long id) {
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        memberRepository.delete(member);
    }

}