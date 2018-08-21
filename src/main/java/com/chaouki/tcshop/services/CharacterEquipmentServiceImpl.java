package com.chaouki.tcshop.services;

import com.chaouki.tcshop.dao.CharacterEquipmentDao;
import com.chaouki.tcshop.dao.ItemTemplateDao;
import com.chaouki.tcshop.entities.Character;
import com.chaouki.tcshop.entities.CharacterEquipment;
import com.chaouki.tcshop.entities.CharacterEquipmentSlot;
import com.chaouki.tcshop.entities.enums.EquipmentSlot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CharacterEquipmentServiceImpl implements CharacterEquipmentService {

    private final CharacterEquipmentDao characterEquipmentDao;
    private final CharacterService characterService;
    private final ItemTemplateDao itemTemplateDao;

    @Autowired
    public CharacterEquipmentServiceImpl(CharacterEquipmentDao characterEquipmentDao, CharacterService characterService, ItemTemplateDao itemTemplateDao) {
        this.characterEquipmentDao = characterEquipmentDao;
        this.characterService = characterService;
        this.itemTemplateDao = itemTemplateDao;
    }

    @Override
    public void updateEquipment(Integer characterId, LocalDateTime timestamp, Map<Integer, Integer> itemMap) {
        Character character = characterService.findByIdIncludeStub(characterId).orElseGet(() -> characterService.createStubCharacter(characterId));

        // either it's a duplicate message. or an out of order message. in both case: just ignore the message.
        if(character.getEquipmentUpdateTimestamp() != null && character.getEquipmentUpdateTimestamp().compareTo(timestamp) >= 0)
            return;

        characterEquipmentDao.deleteByCharacter(character);
        for (Map.Entry<Integer, Integer> integerIntegerEntry : itemMap.entrySet()) {
            CharacterEquipment characterEquipment = new CharacterEquipment();
            characterEquipment.setItemTemplate(itemTemplateDao.getOne(integerIntegerEntry.getValue()));
            CharacterEquipmentSlot slot = new CharacterEquipmentSlot(character, EquipmentSlot.getByIndex(integerIntegerEntry.getKey()));
            characterEquipment.setSlot(slot);
            characterEquipmentDao.save(characterEquipment);
        }

        characterService.onEquipmentUpdate(character, timestamp);
    }

    @Override
    public List<CharacterEquipment> findByCharacter(Character character) {
        return characterEquipmentDao.findByCharacter(character);
    }

    @Override
    public void deleteByCharacter(Character character) {
        characterEquipmentDao.deleteByCharacter(character);
    }
}
