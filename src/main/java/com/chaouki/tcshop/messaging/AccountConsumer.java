package com.chaouki.tcshop.messaging;

import com.chaouki.tcshop.services.AccountService;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Properties;

@Component
public class AccountConsumer implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountConsumer.class);

    private final static String TOPIC = "ACCOUNT";

    @Value("${kafka.bootstrap-servers}")
    private String BOOTSTRAP_SERVERS;

    private Consumer<String, String> consumer;

    @Autowired
    private AccountService accountService;

    @PostConstruct
    public void init() {

        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "tc-shop");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // Create the consumer using props.
        consumer = new KafkaConsumer<>(props);

        // Subscribe to the topic.
        consumer.subscribe(Collections.singletonList(TOPIC));
    }

    @Override
    public void run() {

        while (true) {
            final ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : consumerRecords) {
                try {
                    LOGGER.info("Consumer Record:({}, {}, {}, {}, {})", record.key(), record.value(), record.partition(), record.offset(), TOPIC);

                    AccountDTO accountDTO = parseMessage(record.value());
                    accountService.onAccountMessage(accountDTO.id, accountDTO.timestamp, accountDTO.username, accountDTO.hashedPassword);

                } catch (RuntimeException e) {
                    LOGGER.error("exception raised on account message processing", e);
                }
            }
            consumer.commitAsync();
        }
    }

    private AccountDTO parseMessage(String value) {
        String[] tokens = value.split("#");
        if (tokens.length != 4)
            throw new IllegalArgumentException(value + " payload is invalid for topic " + TOPIC);

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.id = Integer.valueOf(tokens[0]);
        Long timestamp = Long.valueOf(tokens[1]);
        accountDTO.timestamp = LocalDateTime.ofEpochSecond(timestamp / 1_000_000_000, (int) (timestamp % 1_000_000_000), ZoneOffset.UTC);
        accountDTO.username = tokens[2];
        accountDTO.hashedPassword = tokens[3];
        return accountDTO;
    }

    @PreDestroy
    public void destroy() {
        consumer.close();
    }

    private class AccountDTO {
        Integer id;
        LocalDateTime timestamp;
        String username;
        String hashedPassword;
    }
}
