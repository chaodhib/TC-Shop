package com.chaouki.tcshop.messaging;

import com.chaouki.tcshop.entities.Order;
import com.chaouki.tcshop.services.CharacterEquipmentService;
import com.chaouki.tcshop.services.OrderService;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class GearPurchaseAckConsumer implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(GearPurchaseAckConsumer.class);

    private final static String TOPIC = "GEAR_PURCHASE_ACK";

    @Value("${kafka.bootstrap-servers}")
    private String BOOTSTRAP_SERVERS;

    private Consumer<String, String> consumer;

    @Autowired
    private OrderService orderService;

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

                    GearPurchaseAckDTO dto = parseMessage(record.value());
                    if (dto.success) {
                        Order order = orderService.findById(dto.orderId);
                        orderService.flagOrderAsSentToGameServer(order);
                    }
                } catch (RuntimeException e) {
                    LOGGER.error("exception raised on gear snapshot message processing", e);
                }
            }
            consumer.commitAsync();
        }
    }

    private GearPurchaseAckDTO parseMessage(String value) {
        String[] tokens = value.split("#");
        if (tokens.length != 2)
            throw new IllegalArgumentException(value + " payload is invalid for topic " + TOPIC);

        GearPurchaseAckDTO gearSnapshotDTO = new GearPurchaseAckDTO();
        gearSnapshotDTO.orderId = Integer.valueOf(tokens[0]);
        gearSnapshotDTO.success = Boolean.parseBoolean(tokens[1]);
        return gearSnapshotDTO;
    }

    @PreDestroy
    public void destroy() {
        consumer.close();
    }

    private class GearPurchaseAckDTO {
        Integer orderId;
        boolean success;
    }
}
