package com.SJY.O2O_Automatic_Store_System_Demo.service.alarm;

import com.SJY.O2O_Automatic_Store_System_Demo.dto.alarm.AlarmInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Qualifier("LineAlarmService")
public class LineAlarmService implements AlarmService {
    @Override
    public void alarm(AlarmInfoDto infoDto) {
        log.info("{} 에게 라인 전송 = {}", infoDto.getTarget().getNickname(), infoDto.getMessage());
    }
}