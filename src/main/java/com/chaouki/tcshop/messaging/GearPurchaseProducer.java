package com.chaouki.tcshop.messaging;

import com.chaouki.tcshop.entities.Order;
import com.chaouki.tcshop.entities.OrderLine;
import com.chaouki.tcshop.services.OrderService;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Properties;

@Component
public class GearPurchaseProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(GearPurchaseProducer.class);

    private final static String TOPIC = "GEAR_PURCHASE";

    @Value("${kafka.bootstrap-servers}")
    private String BOOTSTRAP_SERVERS;

    private Producer<String, String> producer;

    @PostConstruct
    public void init() {

        final Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "tc-shop");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 1000);
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.ACKS_CONFIG, "all");

        // Create the consumer using props.
        producer = new KafkaProducer<>(props);
    }

    @PreDestroy
    public void prepDestroy() {
        producer.close();
    }

    public void sendGearPurchaseMessage(Order order, Callback callback) {
        final ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, constructMessage(order));
        LOGGER.info("Consumer Record:({}, {}, {}, {}, {})", record.key(), record.value(), record.partition(), null, TOPIC);
        producer.send(record, callback);
    }

    private String constructMessage(Order order) {
        Assert.notEmpty(order.getOrderLineList(), "the order shouldn't be empty");

        StringBuilder sb = new StringBuilder();

        sb.append(order.getId());
        sb.append("#");

        sb.append(order.getCharacter().getId());

        for (OrderLine orderLine : order.getOrderLineList()) {
            sb.append("#");
            sb.append(orderLine.getPurchasableItem().getItemTemplate().getEntry());
            sb.append(":");
            sb.append(orderLine.getQuantity());
        }

        return sb.toString();
    }
}
