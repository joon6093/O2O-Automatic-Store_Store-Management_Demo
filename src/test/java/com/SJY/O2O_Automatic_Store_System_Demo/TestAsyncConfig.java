package com.SJY.O2O_Automatic_Store_System_Demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@Profile("test")
@Slf4j
public class TestAsyncConfig implements AsyncConfigurer {

    @Bean(name = "handleAlarm")
    @Override
    public Executor getAsyncExecutor() {
        return command -> {
            log.info("Starting async task in the same thread");
            command.run();
            log.info("Async task completed");
        };
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> log.info("exception occurred in {} {} : {}", method.getName(), params, ex.getMessage());
    }
}