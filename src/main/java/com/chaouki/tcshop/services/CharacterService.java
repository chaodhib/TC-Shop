package com.chaouki.tcshop.services;

import com.chaouki.tcshop.entities.Account;
import com.chaouki.tcshop.entities.Character;
import com.chaouki.tcshop.entities.enums.CharacterClass;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CharacterService {

    Character createCharacter(Integer accountId, Integer characterId, String characterName, CharacterClass characterClass);

    Optional<Character> findById(Integer id);

    void onEquipmentUpdate(Character character, LocalDateTime timestamp);

    List<Character> findByAccount(Account account);

    void deleteCharacter(Integer accountId, Integer id);
}
