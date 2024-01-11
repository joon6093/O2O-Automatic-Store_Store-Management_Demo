package com.SJY.O2O_Automatic_Store_System_Demo.service.alarm;

import com.SJY.O2O_Automatic_Store_System_Demo.dto.alarm.AlarmInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Qualifier("SmsAlarmService")
public class SmsAlarmService implements AlarmService {
    @Override
    public void alarm(AlarmInfoDto infoDto) {
        log.info("{} 에게 문자메시지 전송 = {}", infoDto.getTarget().getUsername(), infoDto.getMessage());
    }
}