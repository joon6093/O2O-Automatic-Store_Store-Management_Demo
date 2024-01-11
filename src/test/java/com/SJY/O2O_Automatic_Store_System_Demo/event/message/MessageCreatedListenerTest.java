package com.SJY.O2O_Automatic_Store_System_Demo.event.message;

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
class MessageCreatedListenerTest {

    @Autowired
    private ApplicationEventPublisher publisher;

    @MockBean(name = "emailAlarmService")
    private AlarmService emailAlarmService;
    @MockBean(name = "lineAlarmService")
    private AlarmService lineAlarmService;
    @MockBean(name = "smsAlarmService")
    private AlarmService smsAlarmService;

    int calledCount;

    @AfterTransaction
    void afterEach() {
        verify(emailAlarmService, times(calledCount)).alarm(any(AlarmInfoDto.class));
        verify(lineAlarmService, times(calledCount)).alarm(any(AlarmInfoDto.class));
        verify(smsAlarmService, times(calledCount)).alarm(any(AlarmInfoDto.class));
    }

    @Test
    void handleMessageCreatedEventTest() {
        // given
        MemberDto publisher = MemberDto.toDto(createMemberWithId(1L));
        MemberDto receiver = MemberDto.toDto(createMemberWithId(2L));
        String content = "Hello!";

        // when
        this.publisher.publishEvent(new MessageCreatedEvent(publisher, receiver, content));

        // then
        calledCount = 1;
    }
}