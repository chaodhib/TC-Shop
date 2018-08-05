package com.chaouki.tcshop.services;

import com.chaouki.tcshop.entities.Character;
import com.chaouki.tcshop.entities.CharacterClass;

public interface CharacterService {

    Character createCharacter(Integer accountId, Integer characterId, String characterName, CharacterClass characterClass);
}
