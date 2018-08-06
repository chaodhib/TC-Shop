package com.chaouki.tcshop.messaging;

import com.chaouki.tcshop.entities.CharacterEquipment;
import com.chaouki.tcshop.services.AccountService;
import com.chaouki.tcshop.services.CharacterEquipmentService;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component
public class GearSnapshotConsumer implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(GearSnapshotConsumer.class);

    private final static String TOPIC = "GEAR_SNAPSHOT";
    private final static String BOOTSTRAP_SERVERS = "localhost:9092";
    private Consumer<String, String> consumer;

    @Autowired
    private CharacterEquipmentService characterEquipmentService;

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
                    LOGGER.info("Consumer Record:({}, {}, {}, {}, {})", record.key(), record.value(), record.partition(), record.offset(), TOPIC);

                    GearSnapshotDTO gearSnapshotDTO = parseMessage(record.value());
                    characterEquipmentService.updateEquipment(gearSnapshotDTO.characterId, gearSnapshotDTO.timestamp, gearSnapshotDTO.itemMap);

                } catch (RuntimeException e) {
                    LOGGER.error("exception raised on gear snapshot message processing", e);
                }
            }
            consumer.commitAsync();
        }
    }

    private GearSnapshotDTO parseMessage(String value) {
        String[] tokens = value.split("#");
        if (tokens.length < 2)
            throw new IllegalArgumentException(value + " payload is invalid for topic " + TOPIC);

        GearSnapshotDTO gearSnapshotDTO = new GearSnapshotDTO();
        gearSnapshotDTO.characterId = Integer.valueOf(tokens[0]);
        gearSnapshotDTO.timestamp = LocalDateTime.ofEpochSecond(Long.valueOf(tokens[1]) / 1_000_000_000, 0, ZoneOffset.UTC);
        HashMap<Integer, Integer> itemMap = new HashMap<>();
        for (int i = 2; i < tokens.length; i++) {
            String[] split = tokens[i].split(":");
            itemMap.put(Integer.valueOf(split[0]), Integer.valueOf(split[1]));
        }

        gearSnapshotDTO.itemMap = itemMap;
        return gearSnapshotDTO;
    }

    @PreDestroy
    public void destroy() {
        consumer.close();
    }

    private class GearSnapshotDTO {
        Integer characterId;
        LocalDateTime timestamp;
        Map<Integer, Integer> itemMap; // key: slotId. value: itemTemplateId
    }
}
