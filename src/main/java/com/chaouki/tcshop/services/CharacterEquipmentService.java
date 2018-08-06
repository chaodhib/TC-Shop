package com.chaouki.tcshop.services;

import java.time.LocalDateTime;
import java.util.Map;

public interface CharacterEquipmentService {
    void updateEquipment(Integer characterId, LocalDateTime timestamp, Map<Integer, Integer> itemMap);
}
