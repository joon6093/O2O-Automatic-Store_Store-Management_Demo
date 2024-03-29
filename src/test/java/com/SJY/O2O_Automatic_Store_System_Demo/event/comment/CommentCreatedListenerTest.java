package com.SJY.O2O_Automatic_Store_System_Demo.event.comment;

import com.SJY.O2O_Automatic_Store_System_Demo.dto.alarm.AlarmInfoDto;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.member.MemberDto;
import com.SJY.O2O_Automatic_Store_System_Demo.service.alarm.AlarmService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.transaction.annotation.Transactional;

import static com.SJY.O2O_Automatic_Store_System_Demo.factory.entity.MemberFactory.createMemberWithId;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@SpringBootTest
@ActiveProfiles(value = "test")
@Transactional
@Commit
class CommentCreatedListenerTest {
    @Autowired
    ApplicationEventPublisher publisher;
    @MockBean(name = "smsAlarmService")
    AlarmService smsAlarmService;
    @MockBean(name = "emailAlarmService")
    AlarmService emailAlarmService;
    @MockBean(name = "lineAlarmService")
    AlarmService lineAlarmService;

    int calledCount;

    @AfterTransaction
    void afterEach() {
        verify(emailAlarmService, times(calledCount)).alarm(any(AlarmInfoDto.class));
        verify(lineAlarmService, times(calledCount)).alarm(any(AlarmInfoDto.class));
        verify(smsAlarmService, times(calledCount)).alarm(any(AlarmInfoDto.class));
    }

    @Test
    void handleCommentCreatedEventTest() {
        // given
        MemberDto publisher = MemberDto.toDto(createMemberWithId(1L));
        MemberDto postWriter = MemberDto.toDto(createMemberWithId(2L));
        MemberDto parentWriter = MemberDto.toDto(createMemberWithId(3L));
        String content = "content";

        // when
        this.publisher.publishEvent(new CommentCreatedEvent(publisher, postWriter, parentWriter, content));

        //then
        calledCount = 2;
    }

    @Test
    void handleCommentCreatedEventWhenPublisherIsPostWriterTest() {
        // given
        MemberDto publisher = MemberDto.toDto(createMemberWithId(1L));
        MemberDto postWriter = MemberDto.toDto(createMemberWithId(1L));
        MemberDto parentWriter = MemberDto.empty();
        String content = "content";

        // when
        this.publisher.publishEvent(new CommentCreatedEvent(publisher, postWriter, parentWriter, content));

        // then
        calledCount = 0;
    }

    @Test
    void handleCommentCreatedEventWhenPublisherIsParentWriterTest() {
        // given
        MemberDto publisher = MemberDto.toDto(createMemberWithId(1L));
        MemberDto postWriter = MemberDto.toDto(createMemberWithId(2L));
        MemberDto parentWriter = MemberDto.toDto(createMemberWithId(1L));
        String content = "content";

        // when
        this.publisher.publishEvent(new CommentCreatedEvent(publisher, postWriter, parentWriter, content));

        // then
        calledCount = 1;
    }

    @Test
    void handleCommentCreatedEventWhenPostWriterIsParentWriterTest() {
        // given
        MemberDto publisher = MemberDto.toDto(createMemberWithId(1L));
        MemberDto postWriter = MemberDto.toDto(createMemberWithId(2L));
        MemberDto parentWriter = MemberDto.toDto(createMemberWithId(2L));
        String content = "content";

        // when
        this.publisher.publishEvent(new CommentCreatedEvent(publisher, postWriter, parentWriter, content));

        // then
        calledCount = 1;
    }
}