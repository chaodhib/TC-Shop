package com.chaouki.tcshop.messaging;

import com.chaouki.tcshop.entities.CharacterClass;
import com.chaouki.tcshop.services.AccountService;
import com.chaouki.tcshop.services.CharacterService;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Properties;

@Component
public class CharacterConsumer implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(CharacterConsumer.class);

    private final static String TOPIC = "CHARACTER";
    private static final int PAYLOAD_TOKEN_NUMBER = 4;
    private final static String BOOTSTRAP_SERVERS = "localhost:9092";

    private Consumer<String, String> consumer;

    @Autowired
    private CharacterService characterService;

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
            final ConsumerRecords<String, String> consumerRecords = consumer.poll(1000);
            for (ConsumerRecord<String, String> record : consumerRecords) {
                try {
                    LOGGER.info("Consumer Record:({}, {}, {}, {})", record.key(), record.value(), record.partition(), record.offset());

                    CharacterDTO characterDTO = parseMessage(record.value());
                    characterService.createCharacter(characterDTO.accountId, characterDTO.id, characterDTO.name, characterDTO.characterClass);
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
        characterDTO.accountId = Integer.valueOf(tokens[0]);
        characterDTO.id = Integer.valueOf(tokens[1]);
        characterDTO.name = tokens[2];
        characterDTO.characterClass = CharacterClass.getByIndex(Integer.valueOf(tokens[3]));
        return characterDTO;
    }

    @PreDestroy
    public void destroy() {
        consumer.close();
    }

    private class CharacterDTO {
        Integer id;
        Integer accountId;
        String name;
        CharacterClass characterClass;
    }
}
