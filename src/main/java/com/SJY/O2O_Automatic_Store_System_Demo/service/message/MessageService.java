package com.SJY.O2O_Automatic_Store_System_Demo.service.message;

import com.SJY.O2O_Automatic_Store_System_Demo.dto.member.MemberDto;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.message.*;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.member.Member;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.message.Message;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.MemberNotFoundException;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.MessageNotFoundException;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.member.MemberRepository;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.message.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher publisher;

    public MessageListDto readAllBySender(MessageReadCondition cond) {
        Slice<MessageSimpleDto> slice = messageRepository.findAllBySenderIdOrderByMessageIdDesc(
                cond.getMemberId(), cond.getLastMessageId(), Pageable.ofSize(cond.getSize())
        );
        return MessageListDto.builder()
                .numberOfElements(slice.getNumberOfElements())
                .hasNext(slice.hasNext())
                .messageList(slice.getContent())
                .build();
    }

    public MessageListDto readAllByReceiver(MessageReadCondition cond) {
        Slice<MessageSimpleDto> slice = messageRepository.findAllByReceiverIdOrderByMessageIdDesc(
                cond.getMemberId(), cond.getLastMessageId(), Pageable.ofSize(cond.getSize())
        );
        return MessageListDto.builder()
                .numberOfElements(slice.getNumberOfElements())
                .hasNext(slice.hasNext())
                .messageList(slice.getContent())
                .build();
    }

    @PreAuthorize("@messageGuard.check(#id)")
    public MessageDto read(@Param("id")Long id) {
        Message message = messageRepository.findWithSenderAndReceiverById(id).orElseThrow(MessageNotFoundException::new);
        return MessageDto.builder()
                .id(message.getId())
                .content(message.getContent())
                .sender(MemberDto.toDto(message.getSender()))
                .receiver(MemberDto.toDto(message.getReceiver()))
                .createdAt(message.getCreatedAt())
                .build();
    }

    @Transactional
    public MessageCreateResponse create(MessageCreateRequest req) {
        Member sender = memberRepository.findById(req.getMemberId())
                .orElseThrow(MemberNotFoundException::new);
        Member receiver = memberRepository.findById(req.getReceiverId())
                .orElseThrow(MemberNotFoundException::new);
        Message message = Message.builder()
                            .content(req.getContent())
                            .sender(sender)
                            .receiver(receiver)
                            .build();
        messageRepository.save(message);
        message.publishCreatedEvent(publisher);
        return new MessageCreateResponse(message.getId());
    }

    @PreAuthorize("@messageSenderGuard.check(#id)")
    @Transactional
    public void deleteBySender(@Param("id")Long id) {
        delete(id, message -> {
            message.deleteBySender();
        });
    }

    @PreAuthorize("@messageReceiverGuard.check(#id)")
    @Transactional
    public void deleteByReceiver(@Param("id")Long id) {
        delete(id, message -> {
            message.deleteByReceiver();
        });
    }

    private void delete(Long id, Consumer<Message> delete) {
        Message message = messageRepository.findById(id).orElseThrow(MessageNotFoundException::new);
        delete.accept(message);
        if (message.isDeletable()) {
            messageRepository.delete(message);
        }
    }
}