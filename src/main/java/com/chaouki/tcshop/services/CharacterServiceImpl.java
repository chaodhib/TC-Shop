package com.chaouki.tcshop.services;

import com.chaouki.tcshop.dao.CharacterDao;
import com.chaouki.tcshop.entities.Account;
import com.chaouki.tcshop.entities.Character;
import com.chaouki.tcshop.entities.enums.CharacterClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    public void createCharacter(Integer accountId, Integer characterId, String characterName, CharacterClass characterClass) {

        if(characterDao.findById(characterId).isPresent()){
            // two case:
            // 1) it's an out of order situation: a delete character message was received before the creation character message.
            // 2) it's a duplicate: two creation messages were received. in that case, just ignore the second message.
            // in both cases, there is nothing more to do.
            return;
        }

        Account account = accountService.findById(accountId).orElseThrow(IllegalArgumentException::new);

        Character character = new Character();
        character.setId(characterId);
        character.setAccount(account);
        character.setName(characterName);
        character.setCharacterClass(characterClass);
        characterDao.save(character);
    }

    @Override
    public Optional<Character> findById(Integer id) {
        Optional<Character> characterOptional = characterDao.findById(id);
        if(characterOptional.isPresent() && characterOptional.get().isDeleted())
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
    public void deleteCharacter(Integer accountId, Integer characterId) {
        Character character = characterDao.findById(characterId).orElseThrow(IllegalArgumentException::new);

        if(character.isDeleted())
            return; // duplicate message. nothing to do then.

        if(!character.getAccount().getId().equals(accountId))
            throw new IllegalArgumentException("the character does not belong to the account provided");

        characterEquipmentService.deleteByCharacter(character);
        character.setDeleted(true);
        characterDao.save(character);
    }
}
