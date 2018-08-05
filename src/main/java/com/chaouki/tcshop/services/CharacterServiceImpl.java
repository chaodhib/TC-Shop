package com.chaouki.tcshop.services;

import com.chaouki.tcshop.dao.CharacterDao;
import com.chaouki.tcshop.entities.Account;
import com.chaouki.tcshop.entities.Character;
import com.chaouki.tcshop.entities.CharacterClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CharacterServiceImpl implements CharacterService {

    @Autowired
    private CharacterDao characterDao;

    @Autowired
    private AccountService accountService;

    @Override
    public Character createCharacter(Integer accountId, Integer characterId, String characterName, CharacterClass characterClass) {
        Account account = accountService.findById(accountId).orElseThrow(IllegalArgumentException::new);

        Character character = new Character();
        character.setId(characterId);
        character.setAccount(account);
        character.setName(characterName);
        character.setCharacterClass(characterClass);
        return characterDao.save(character);
    }
}
