package com.chaouki.tcshop.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.CountDownLatch;

@Component
public class KafkaAsyncConsumerStarter {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaAsyncConsumerStarter.class);

    @Autowired
    private AsyncTaskExecutor taskExecutor;

    @Autowired
    private AccountConsumer accountConsumer;

    @Autowired
    private CharacterConsumer characterConsumer;

    @Autowired
    private GearSnapshotConsumer gearSnapshotConsumer;

    @Autowired
    private GearPurchaseAckConsumer gearPurchaseAckConsumer;

    @Autowired
    private CountDownLatch accountLatch;

    @Autowired
    private CountDownLatch characterLatch;

    @Async
    public void start() {
        try {
            taskExecutor.submit(accountConsumer);
            accountLatch.await();
            taskExecutor.submit(characterConsumer);
            characterLatch.await();
            taskExecutor.submit(gearSnapshotConsumer);
            taskExecutor.submit(gearPurchaseAckConsumer);
        } catch (InterruptedException e) {
            LOGGER.error("error during start of kafka consumers", e);
        }
    }
}
