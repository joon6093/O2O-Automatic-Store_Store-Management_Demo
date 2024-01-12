package com.SJY.O2O_Automatic_Store_System_Demo.service.message;

import com.SJY.O2O_Automatic_Store_System_Demo.dto.message.*;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.message.Message;
import com.SJY.O2O_Automatic_Store_System_Demo.exception.MessageNotFoundException;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.member.MemberRepository;
import com.SJY.O2O_Automatic_Store_System_Demo.repository.message.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
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
        return MessageListDto.toDto(
                messageRepository.findAllBySenderIdOrderByMessageIdDesc(cond.getMemberId(), cond.getLastMessageId(), Pageable.ofSize(cond.getSize()))
        );
    }

    public MessageListDto readAllByReceiver(MessageReadCondition cond) {
        return MessageListDto.toDto(
                messageRepository.findAllByReceiverIdOrderByMessageIdDesc(cond.getMemberId(), cond.getLastMessageId(), Pageable.ofSize(cond.getSize()))
        );
    }

    public MessageDto read(Long id) {
        return MessageDto.toDto(
                messageRepository.findWithSenderAndReceiverById(id).orElseThrow(MessageNotFoundException::new)
        );
    }

    @Transactional
    public MessageCreateResponse create(MessageCreateRequest req) {
        Message message = MessageCreateRequest.toEntity(req, memberRepository);
        messageRepository.save(message);
        message.publishCreatedEvent(publisher);
        return new MessageCreateResponse(message.getId());
    }

    @Transactional
    public void deleteBySender(Long id) {
        delete(id, message -> {
            message.deleteBySender();
        });
    }

    @Transactional
    public void deleteByReceiver(Long id) {
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