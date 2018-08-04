package com.chaouki.tcshop.services;

import com.chaouki.tcshop.entities.CharacterClass;
import com.chaouki.tcshop.entities.Character;
import com.chaouki.tcshop.entities.Gender;
import com.chaouki.tcshop.entities.Race;

import java.util.List;

/**
 * Created by chaouki on 19/03/2018.
 */
public interface CharacterService {

    List<Character> findAll();

    List<Character> findByAccount(Integer account);

    Character create(Integer account, String name, Race race, CharacterClass characterClass, Gender gender);
}