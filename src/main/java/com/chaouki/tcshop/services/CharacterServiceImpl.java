package com.chaouki.tcshop.services;

import com.chaouki.tcshop.dao.CharacterDao;
import com.chaouki.tcshop.entities.Account;
import com.chaouki.tcshop.entities.Character;
import com.chaouki.tcshop.entities.enums.CharacterClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CharacterServiceImpl implements CharacterService {

    private CharacterDao characterDao;
    private AccountService accountService;

    public CharacterServiceImpl(CharacterDao characterDao, AccountService accountService) {
        this.characterDao = characterDao;
        this.accountService = accountService;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
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
            character.setStub(false);
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
            character.setStub(false);
            character = characterDao.save(character);
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
    public Optional<Character> findByIdIncludeStub(Integer id) {
        Optional<Character> characterOptional = characterDao.findById(id);
        if(characterOptional.isPresent() && characterOptional.get().isDeleted())
            return Optional.empty();

        return characterOptional;
    }

    @Override
    public void onEquipmentUpdate(Character character, LocalDateTime timestamp) {
        character.setEquipmentUpdateTimestamp(timestamp);
        characterDao.save(character);
    }

    @Override
    public List<Character> findActiveCharsByAccount(Account account) {
        return characterDao.findActiveCharsByAccount(account);
    }

    @Override
    public Character createStubCharacter(Integer characterId) {
        Character character = new Character();
        character.setId(characterId);
        character.setAccount(accountService.findById(Integer.MAX_VALUE).orElseThrow(IllegalStateException::new));
        character.setName("STUB");
        character.setCharacterClass(CharacterClass.getByIndex(1));
        character.setLastUpdateTimestamp(LocalDateTime.of(2000, 1,1,0,0));
        character.setDeleted(false);
        character.setStub(true);
        return characterDao.save(character);
    }
}
