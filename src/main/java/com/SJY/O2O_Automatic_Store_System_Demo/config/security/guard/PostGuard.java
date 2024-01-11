package com.SJY.O2O_Automatic_Store_System_Demo.config.security.guard;

import com.SJY.O2O_Automatic_Store_System_Demo.entity.member.RoleType;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.post.Post;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PostGuard extends Guard {
    private final PostRepository postRepository;
    private List<RoleType> roleTypes = List.of(RoleType.ROLE_ADMIN);

    @Override
    protected List<RoleType> getRoleTypes() {
        return roleTypes;
    }

    @Override
    protected boolean isResourceOwner(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new AccessDeniedException(""));
        Long memberId = AuthHelper.extractMemberId();
        return post.getMember().getId().equals(memberId);
    }
}