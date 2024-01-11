package com.SJY.O2O_Automatic_Store_System_Demo.config.security.guard;

import com.SJY.O2O_Automatic_Store_System_Demo.entity.member.RoleType;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.message.Message;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.message.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MessageGuard extends Guard{
    private final MessageRepository messageRepository;
    private List<RoleType> roleTypes = List.of(RoleType.ROLE_ADMIN);

    @Override
    protected List<RoleType> getRoleTypes() {
        return roleTypes;
    }

    @Override
    protected boolean isResourceOwner(Long id) {
        Message message = messageRepository.findById(id).orElseThrow(() -> new AccessDeniedException("메시지를 찾을 수 없습니다."));
        Long memberId = AuthHelper.extractMemberId();
        return message.getSender().getId().equals(memberId) || message.getReceiver().getId().equals(memberId);
    }
}