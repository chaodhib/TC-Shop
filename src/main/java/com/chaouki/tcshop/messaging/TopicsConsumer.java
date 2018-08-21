package com.chaouki.tcshop.messaging;

import com.chaouki.tcshop.entities.enums.CharacterClass;
import com.chaouki.tcshop.services.AccountService;
import com.chaouki.tcshop.services.CharacterEquipmentService;
import com.chaouki.tcshop.services.CharacterService;
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
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component
public class TopicsConsumer implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(TopicsConsumer.class);

    private final static String ACCOUNT_TOPIC = "ACCOUNT";
    private final static String CHARACTER_TOPIC = "CHARACTER";
    private final static String GEAR_PURCHASE_ACK_TOPIC = "GEAR_PURCHASE_ACK";
    private final static String GEAR_SNAPSHOT_TOPIC = "GEAR_SNAPSHOT";

    @Value("${kafka.bootstrap-servers}")
    private String BOOTSTRAP_SERVERS;

    private Consumer<String, String> consumer;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CharacterService characterService;

    @Autowired
    private OrderService orderService;

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
        consumer.subscribe(Arrays.asList(ACCOUNT_TOPIC, CHARACTER_TOPIC, GEAR_PURCHASE_ACK_TOPIC, GEAR_SNAPSHOT_TOPIC));
    }

    @PreDestroy
    public void destroy() {
        consumer.close();
    }

    @Override
    public void run() {

        while (true) {
            final ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : consumerRecords) {
                try {
                    LOGGER.info("Consumer Record:({}, {}, {}, {}, {})", record.key(), record.value(), record.partition(), record.offset(), record.topic());

                    switch (record.topic()) {
                        case ACCOUNT_TOPIC:
                            AccountDTO accountDTO = parseAccountMessage(record.value(), record.topic());
                            accountService.onAccountMessage(accountDTO.id, accountDTO.timestamp, accountDTO.username, accountDTO.hashedPassword);
                            break;
                        case CHARACTER_TOPIC:
                            CharacterDTO characterDTO = parseCharacterMessage(record.value(), record.topic());
                            characterService.onCharacterMessage(characterDTO.accountId, characterDTO.id, characterDTO.timestamp, characterDTO.name, characterDTO.characterClass, characterDTO.enabled);
                            break;
                        case GEAR_SNAPSHOT_TOPIC:
                            GearSnapshotDTO gearSnapshotDTO = parseGearSnapshotMessage(record.value(), record.topic());
                            characterEquipmentService.updateEquipment(gearSnapshotDTO.characterId, gearSnapshotDTO.timestamp, gearSnapshotDTO.itemMap);
                            break;
                        case GEAR_PURCHASE_ACK_TOPIC:
                            GearPurchaseAckDTO dto = parseGearPurchaseAckMessage(record.value(), record.topic());
                            if (dto.success) {
                                orderService.flagOrderAsAcceptedByGameServer(dto.orderId);
                            } else {
                                orderService.flagOrderAsRefusedByGameServer(dto.orderId);
                            }
                            break;
                        default:
                            throw new IllegalArgumentException("Unknown topic!");
                    }

                } catch (RuntimeException e) {
                    LOGGER.error("exception raised on account message processing", e);
                }
            }
            consumer.commitAsync();
        }
    }

    private AccountDTO parseAccountMessage(String value, String topic) {
        String[] tokens = value.split("#");
        if (tokens.length != 4)
            throw new IllegalArgumentException(value + " payload is invalid for topic " + topic);

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.id = Integer.valueOf(tokens[0]);
        Long timestamp = Long.valueOf(tokens[1]);
        accountDTO.timestamp = LocalDateTime.ofEpochSecond(timestamp / 1_000_000_000, (int) (timestamp % 1_000_000_000), ZoneOffset.UTC);
        accountDTO.username = tokens[2];
        accountDTO.hashedPassword = tokens[3];
        return accountDTO;
    }

    private class AccountDTO {
        Integer id;
        LocalDateTime timestamp;
        String username;
        String hashedPassword;
    }

    private CharacterDTO parseCharacterMessage(String value, String topic) {
        String[] tokens = value.split("#");
        if (tokens.length != 6)
            throw new IllegalArgumentException(value + " payload is invalid for topic " + topic);

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

    private class CharacterDTO {
        Integer id;
        Integer accountId;
        LocalDateTime timestamp;
        String name;
        CharacterClass characterClass;
        boolean enabled;
    }

    private GearSnapshotDTO parseGearSnapshotMessage(String value, String topic) {
        String[] tokens = value.split("#");
        if (tokens.length < 2)
            throw new IllegalArgumentException(value + " payload is invalid for topic " + topic);

        GearSnapshotDTO gearSnapshotDTO = new GearSnapshotDTO();
        gearSnapshotDTO.characterId = Integer.valueOf(tokens[0]);
        Long timestamp = Long.valueOf(tokens[1]);
        gearSnapshotDTO.timestamp = LocalDateTime.ofEpochSecond(timestamp / 1_000_000_000, (int) (timestamp % 1_000_000_000), ZoneOffset.UTC);
        HashMap<Integer, Integer> itemMap = new HashMap<>();
        for (int i = 2; i < tokens.length; i++) {
            String[] split = tokens[i].split(":");
            itemMap.put(Integer.valueOf(split[0]), Integer.valueOf(split[1]));
        }

        gearSnapshotDTO.itemMap = itemMap;
        return gearSnapshotDTO;
    }

    private class GearSnapshotDTO {
        Integer characterId;
        LocalDateTime timestamp;
        Map<Integer, Integer> itemMap; // key: slotId. value: itemTemplateId
    }

    private GearPurchaseAckDTO parseGearPurchaseAckMessage(String value, String topic) {
        String[] tokens = value.split("#");
        if (tokens.length != 2)
            throw new IllegalArgumentException(value + " payload is invalid for topic " + topic);

        GearPurchaseAckDTO gearSnapshotDTO = new GearPurchaseAckDTO();
        gearSnapshotDTO.orderId = Integer.valueOf(tokens[0]);
        gearSnapshotDTO.success = Boolean.parseBoolean(tokens[1]);
        return gearSnapshotDTO;
    }

    private class GearPurchaseAckDTO {
        Integer orderId;
        boolean success;
    }
}
