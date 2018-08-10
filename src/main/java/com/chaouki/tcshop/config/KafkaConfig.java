package com.chaouki.tcshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.CountDownLatch;

@Configuration
@EnableScheduling
@EnableAsync
public class KafkaConfig {

    @Bean
    public AsyncTaskExecutor taskExecutor(){
        SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
        taskExecutor.setConcurrencyLimit(4);
        return taskExecutor;
    }

    @Bean
    public CountDownLatch accountLatch() {
        return new CountDownLatch(1);
    }

    @Bean
    public CountDownLatch characterLatch() {
        return new CountDownLatch(1);
    }
}
