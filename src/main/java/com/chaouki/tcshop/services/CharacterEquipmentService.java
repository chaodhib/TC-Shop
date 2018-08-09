package com.chaouki.tcshop.services;

import com.chaouki.tcshop.entities.Character;
import com.chaouki.tcshop.entities.CharacterEquipment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CharacterEquipmentService {
    void updateEquipment(Integer characterId, LocalDateTime timestamp, Map<Integer, Integer> itemMap);

    List<CharacterEquipment> findByCharacter(Character character);

    void deleteByCharacter(Character character);
}
