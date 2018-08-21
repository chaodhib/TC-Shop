package com.chaouki.tcshop.services;

import com.chaouki.tcshop.dao.CharacterDao;
import com.chaouki.tcshop.entities.Account;
import com.chaouki.tcshop.entities.Character;
import com.chaouki.tcshop.entities.enums.CharacterClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CharacterServiceImpl implements CharacterService {

    @Autowired
    private CharacterDao characterDao;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CharacterEquipmentService characterEquipmentService;

    @Override
    public void onCharacterMessage(Integer accountId, Integer characterId, LocalDateTime timestamp, String characterName, CharacterClass characterClass, boolean enabled) {

        Optional<Character> savedCharacter = characterDao.findById(characterId);
        if(savedCharacter.isPresent() && savedCharacter.get().getLastUpdateTimestamp().compareTo(timestamp) >= 0) {
            // two case:
            // 1) it's an out of order situation: a delete character message was received before the creation character message.
            // 2) it's a duplicate: two creation messages were received. in that case, just ignore the second message.
            // in both cases, there is nothing more to do.
            return;
        }

        Account account = accountService.findById(accountId).orElseGet(() -> accountService.createStubAccount(accountId));
        Character character;
        if(savedCharacter.isPresent()) {
            // update character

            character = savedCharacter.get();
            character.setAccount(account);
            character.setName(characterName);
            character.setCharacterClass(characterClass);
            character.setLastUpdateTimestamp(timestamp);
            character.setDeleted(!enabled);
            character = characterDao.save(character);

        } else {
            // create character

            character = new Character();
            character.setId(characterId);
            character.setAccount(account);
            character.setName(characterName);
            character.setCharacterClass(characterClass);
            character.setLastUpdateTimestamp(timestamp);
            character.setDeleted(!enabled);
            character = characterDao.save(character);
        }

        if(!enabled){
            characterEquipmentService.deleteByCharacter(character);
        }
    }

    @Override
    public Optional<Character> findById(Integer id) {
        Optional<Character> characterOptional = characterDao.findById(id);
        if(characterOptional.isPresent() && (characterOptional.get().isDeleted() || characterOptional.get().isStub()))
            return Optional.empty();

        return characterOptional;
    }

    @Override
    public void onEquipmentUpdate(Character character, LocalDateTime timestamp) {
        characterDao.setEquipmentUpdateTimestamp(character, timestamp);
    }

    @Override
    public List<Character> findActiveCharsByAccount(Account account) {
        return characterDao.findActiveCharsByAccount(account);
    }

    @Override
    public Character createStubCharacter(Integer characterId) {
        Character character = new Character();
        character.setId(characterId);
        character.setAccount(accountService.findById(1).orElseThrow(IllegalStateException::new));
        character.setName("STUB");
        character.setCharacterClass(CharacterClass.getByIndex(1));
        character.setLastUpdateTimestamp(LocalDateTime.of(2000, 1,1,0,0));
        character.setDeleted(false);
        character.setStub(true);
        return characterDao.save(character);
    }
}
