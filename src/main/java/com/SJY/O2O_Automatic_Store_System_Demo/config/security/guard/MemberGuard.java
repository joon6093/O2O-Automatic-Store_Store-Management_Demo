package com.SJY.O2O_Automatic_Store_System_Demo.config.security.guard;

import com.SJY.O2O_Automatic_Store_System_Demo.entity.member.RoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class MemberGuard {

    private final AuthHelper authHelper;

    public boolean check(Long id) {
        return authHelper.isAuthenticated() && hasAuthority(id);
    }

    private boolean hasAuthority(Long id) {
        Long memberId = authHelper.extractMemberId();
        Set<RoleType> memberRoles = authHelper.extractMemberRoles();
        return id.equals(memberId) || memberRoles.contains(RoleType.ROLE_ADMIN);
    }
}