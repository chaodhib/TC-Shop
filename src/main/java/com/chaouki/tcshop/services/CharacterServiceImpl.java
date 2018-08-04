package com.chaouki.tcshop.services;

import com.chaouki.tcshop.dao.CharacterDao;
import com.chaouki.tcshop.entities.CharacterClass;
import com.chaouki.tcshop.entities.Gender;
import com.chaouki.tcshop.entities.Race;
import com.chaouki.tcshop.entities.Character;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;

/**
 * Created by chaouki on 19/03/2018.
 */
@Service
@Transactional
public class CharacterServiceImpl implements CharacterService {

    @Autowired
    private CharacterDao characterDao;

    @Override
    public List<Character> findAll() {
        return characterDao.findAll();
    }

    @Override
    public List<Character> findByAccount(Integer account) {
        return characterDao.findByAccount(account);
    }

    @Override
    public Character create(Integer account, String name, Race race, CharacterClass characterClass, Gender gender) {
        List<Character> characterList = characterDao.findAll();
        characterList.sort(Comparator.comparingInt(Character::getGuid));

        Integer nextGuid;
        if(characterList.size() > 0)
            nextGuid = characterList.get(characterList.size() -1).getGuid() +1;
        else
            nextGuid = 0;

        Character character = new Character();
        character.setGuid(nextGuid);
        character.setAccount(account);
        character.setCharacterClass(characterClass);
        character.setName(name);
        character.setRace(race);
        character.setGender(gender);

        // prevent character from falling into ground
        character.setPositionX(-13211.9F);
        character.setPositionY(273.472F);
        character.setPositionZ(21.8575F);

        character.setCinematic(true);

        return characterDao.save(character);
    }
}
