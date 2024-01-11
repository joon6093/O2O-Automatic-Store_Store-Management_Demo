package com.SJY.O2O_Automatic_Store_System_Demo.event.message;

import com.SJY.O2O_Automatic_Store_System_Demo.dto.alarm.AlarmInfoDto;
import com.SJY.O2O_Automatic_Store_System_Demo.dto.member.MemberDto;
import com.SJY.O2O_Automatic_Store_System_Demo.service.alarm.AlarmService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class MessageCreatedListener {
    private final AlarmService emailAlarmService;
    private final AlarmService lineAlarmService;
    private final AlarmService smsAlarmService;

    public MessageCreatedListener(@Qualifier("emailAlarmService")AlarmService emailAlarmService, @Qualifier("lineAlarmService")AlarmService lineAlarmService, @Qualifier("smsAlarmService")AlarmService smsAlarmService) {
        this.emailAlarmService = emailAlarmService;
        this.lineAlarmService = lineAlarmService;
        this.smsAlarmService = smsAlarmService;
    }

    private List<AlarmService> alarmServices = new ArrayList<>();

    @PostConstruct
    public void postConstruct() {
        alarmServices.add(emailAlarmService);
        alarmServices.add(lineAlarmService);
        alarmServices.add(smsAlarmService);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    @Async("handleAlarm")
    public void handleAlarm(MessageCreatedEvent event) {
        log.info("CommentCreatedListener.handleAlarm");
        String message = generateAlarmMessage(event);
        if(!isSameMember(event.getPublisher(), event.getReceiver())) alarmTo(event.getReceiver(), message);
    }

    private void alarmTo(MemberDto memberDto, String message) {
        alarmServices.forEach(alarmService -> alarmService.alarm(new AlarmInfoDto(memberDto, message)));
    }

    private boolean isSameMember(MemberDto a, MemberDto b) {
        return Objects.equals(a.getId(), b.getId());
    }

    private String generateAlarmMessage(MessageCreatedEvent event) {
        return event.getPublisher().getNickname() + " : " + event.getContent();
    }
}
