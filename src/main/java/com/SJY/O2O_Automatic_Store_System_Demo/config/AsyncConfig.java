package com.SJY.O2O_Automatic_Store_System_Demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
@Profile("!test")
@Slf4j
public class AsyncConfig implements AsyncConfigurer {

    @Bean(name = "handleAlarm")
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(3); // 초기 스레드 개수
        taskExecutor.setMaxPoolSize(10); // corePoolSize가 모두 사용 중일 때 새로 만들어지는 최대 스레드 개수
        taskExecutor.setQueueCapacity(50); // maxPoolSize가 모두 사용 중일 때 대기하는 큐의 크기
        taskExecutor.setThreadNamePrefix("handleAlarm-thread-");
        taskExecutor.initialize();
        return taskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() { //비동기 메소드에서도 단일화된 예외 관리
        return (ex, method, params) -> log.info("exception occurred in {} {} : {}", method.getName(), params, ex.getMessage());
    }
}