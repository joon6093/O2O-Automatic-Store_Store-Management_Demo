package com.SJY.O2O_Automatic_Store_System_Demo.event.comment;


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
public class CommentCreatedListener {
    private final AlarmService emailAlarmService;
    private final AlarmService lineAlarmService;
    private final AlarmService smsAlarmService;

    public CommentCreatedListener(@Qualifier("emailAlarmService")AlarmService emailAlarmService, @Qualifier("lineAlarmService")AlarmService lineAlarmService, @Qualifier("smsAlarmService")AlarmService smsAlarmService) {
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
    public void handleAlarm(CommentCreatedEvent event) {
        log.info("CommentCreatedListener.handleAlarm");
        String message = generateAlarmMessage(event);
        if(isAbleToSendToPostWriter(event)) alarmTo(event.getPostWriter(), message);
        if(isAbleToSendToParentWriter(event)) alarmTo(event.getParentWriter(), message);
    }

    private void alarmTo(MemberDto memberDto, String message) {
        alarmServices.forEach(alarmService -> alarmService.alarm(new AlarmInfoDto(memberDto, message)));
    }

    private boolean isAbleToSendToPostWriter(CommentCreatedEvent event) {
        if(!isSameMember(event.getPublisher(), event.getPostWriter())) {
            if(hasParent(event))
                return !isSameMember(event.getPostWriter(), event.getParentWriter());
            return true;
        }
        return false;
    }

    private boolean isAbleToSendToParentWriter(CommentCreatedEvent event) {
        return hasParent(event) && !isSameMember(event.getPublisher(), event.getParentWriter());
    }

    private boolean isSameMember(MemberDto a, MemberDto b) {
        return Objects.equals(a.getId(), b.getId());
    }

    private boolean hasParent(CommentCreatedEvent event) {
        return event.getParentWriter().getId() != null;
    }

    private String generateAlarmMessage(CommentCreatedEvent event) {
        return event.getPublisher().getNickname() + " : " + event.getContent();
    }
}