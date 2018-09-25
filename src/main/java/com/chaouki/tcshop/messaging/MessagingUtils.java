package com.chaouki.tcshop.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.CannotAcquireLockException;

public class MessagingUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessagingUtils.class);

    public static void doAndRetry(Runnable toRun, int retryCount) {
        do{
            try {
                toRun.run();
                retryCount = 0;
            } catch (CannotAcquireLockException e) {
                LOGGER.warn("DB lock issue detected", e);
                retryCount--;

                if(retryCount == 0)
                    throw new RuntimeException(e);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    LOGGER.error("interrupted", e1);
                    return;
                }
            }
        } while(retryCount > 0);
    }
}
