package com.chaouki.tcshop.services;

import com.chaouki.tcshop.dao.CharacterDao;
import com.chaouki.tcshop.entities.Account;
import com.chaouki.tcshop.entities.Character;
import com.chaouki.tcshop.entities.enums.CharacterClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @Override
    public Optional<Character> findById(Integer id) {
        return characterDao.findById(id);
    }

    @Override
    public void onEquipmentUpdate(Character character, LocalDateTime timestamp) {
        characterDao.setEquipmentUpdateTimestamp(character, timestamp);
    }

    @Override
    public List<Character> findByAccount(Account account) {
        return characterDao.findByAccount(account);
    }
}
