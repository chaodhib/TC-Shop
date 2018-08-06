package com.chaouki.tcshop.services;

import com.chaouki.tcshop.entities.Character;
import com.chaouki.tcshop.entities.CharacterClass;

import java.time.LocalDateTime;
import java.util.Optional;

public interface CharacterService {

    Character createCharacter(Integer accountId, Integer characterId, String characterName, CharacterClass characterClass);

    Optional<Character> findById(Integer id);

    void onEquipmentUpdate(Character character, LocalDateTime timestamp);
}
