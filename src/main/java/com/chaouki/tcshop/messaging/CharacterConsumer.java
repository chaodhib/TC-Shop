package com.chaouki.tcshop.messaging;

import com.chaouki.tcshop.entities.enums.CharacterClass;
import com.chaouki.tcshop.services.CharacterService;
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
import java.util.concurrent.CountDownLatch;

@Component
public class CharacterConsumer implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(CharacterConsumer.class);

    private final static String TOPIC = "CHARACTER";
    private static final int PAYLOAD_TOKEN_NUMBER = 6;

    @Value("${kafka.bootstrap-servers}")
    private String BOOTSTRAP_SERVERS;

    private Consumer<String, String> consumer;

    @Autowired
    private CharacterService characterService;

    @Autowired
    private CountDownLatch characterLatch;

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
            if(consumerRecords.isEmpty() && characterLatch.getCount() == 1){
                characterLatch.countDown();
                LOGGER.info("Initial Account processing finished");
            }

            for (ConsumerRecord<String, String> record : consumerRecords) {
                try {
                    LOGGER.info("Consumer Record:({}, {}, {}, {}, {})", record.key(), record.value(), record.partition(), record.offset(), TOPIC);

                    CharacterDTO characterDTO = parseMessage(record.value());
                    characterService.onCharacterMessage(characterDTO.accountId, characterDTO.id, characterDTO.timestamp, characterDTO.name, characterDTO.characterClass, characterDTO.enabled);
                } catch (RuntimeException e) {
                    LOGGER.error("exception raised on character message processing", e);
                }
            }
            consumer.commitAsync();
        }
    }

    private CharacterDTO parseMessage(String value) {
        String[] tokens = value.split("#");
        if (tokens.length != PAYLOAD_TOKEN_NUMBER)
            throw new IllegalArgumentException(value + " payload is invalid for topic " + TOPIC);

        CharacterDTO characterDTO = new CharacterDTO();
        characterDTO.id = Integer.valueOf(tokens[0]);
        Long timestamp = Long.valueOf(tokens[1]);
        characterDTO.timestamp = LocalDateTime.ofEpochSecond(timestamp / 1_000_000_000, (int) (timestamp % 1_000_000_000), ZoneOffset.UTC);
        characterDTO.accountId = Integer.valueOf(tokens[2]);
        characterDTO.name = tokens[3];
        characterDTO.characterClass = CharacterClass.getByIndex(Integer.valueOf(tokens[4]));
        characterDTO.enabled = "1".equals(tokens[5]);
        return characterDTO;
    }

    @PreDestroy
    public void destroy() {
        consumer.close();
    }

    private class CharacterDTO {
        Integer id;
        Integer accountId;
        LocalDateTime timestamp;
        String name;
        CharacterClass characterClass;
        boolean enabled;
    }
}
