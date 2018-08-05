package com.chaouki.tcshop.dao;

import com.chaouki.tcshop.entities.Account;
import com.chaouki.tcshop.entities.Character;
import com.chaouki.tcshop.entities.CharacterEquipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CharacterEquipmentDao extends JpaRepository<CharacterEquipment, Integer> {
    List<CharacterEquipment> findByCharacter(Character character);
}
