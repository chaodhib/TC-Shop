package com.chaouki.tcshop.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;

@Component
public class KafkaInitializer {

    @Autowired
    private AsyncTaskExecutor taskExecutor;

    @Autowired
    private AccountConsumer accountConsumer;

    @PostConstruct
    public void init(){
        taskExecutor.submit(accountConsumer);
    }
}
