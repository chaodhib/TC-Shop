package com.chaouki.tcshop.services;

import com.chaouki.tcshop.entities.Account;
import com.chaouki.tcshop.entities.Character;
import com.chaouki.tcshop.entities.enums.CharacterClass;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CharacterService {

    void onCharacterMessage(Integer accountId, Integer characterId, LocalDateTime timestamp, String characterName, CharacterClass characterClass, boolean enabled);

    Optional<Character> findById(Integer id);

    Optional<Character> findByIdIncludeStub(Integer id);

    void onEquipmentUpdate(Character character, LocalDateTime timestamp);

    List<Character> findActiveCharsByAccount(Account account);

    Character createStubCharacter(Integer characterId);
}
