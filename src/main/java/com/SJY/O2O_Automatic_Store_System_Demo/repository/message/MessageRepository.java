package com.SJY.O2O_Automatic_Store_System_Demo.repository.message;

import com.SJY.O2O_Automatic_Store_System_Demo.dto.message.MessageSimpleDto;
import com.SJY.O2O_Automatic_Store_System_Demo.entity.message.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("select m from Message m left join fetch m.sender left join fetch m.receiver where m.id = :id")
    Optional<Message> findWithSenderAndReceiverById(@Param("id")Long id);

    @Query("select new com.SJY.O2O_Automatic_Store_System_Demo.dto.message.MessageSimpleDto(m.id, m.content, m.receiver.nickname, m.createdAt) " +
            "from Message m left join m.receiver " +
            "where m.sender.id = :senderId and m.id < :lastMessageId and m.deletedBySender = false order by m.id desc")
    Slice<MessageSimpleDto> findAllBySenderIdOrderByMessageIdDesc(@Param("senderId")Long senderId, @Param("lastMessageId")Long lastMessageId, Pageable pageable);

    // 2
    @Query("select new com.SJY.O2O_Automatic_Store_System_Demo.dto.message.MessageSimpleDto(m.id, m.content, m.sender.nickname, m.createdAt) " +
            "from Message m left join m.sender " +
            "where m.receiver.id = :receiverId and m.id < :lastMessageId and m.deletedByReceiver = false order by m.id desc")
    Slice<MessageSimpleDto> findAllByReceiverIdOrderByMessageIdDesc(@Param("receiverId")Long receiverId, @Param("lastMessageId")Long lastMessageId, Pageable pageable);
}