package com.chaouki.tcshop.dao;

import com.chaouki.tcshop.entities.Character;
import com.chaouki.tcshop.entities.CharacterEquipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CharacterEquipmentDao extends JpaRepository<CharacterEquipment, Integer> {

    @Query("DELETE FROM CharacterEquipment WHERE slot.character = ?1")
    @Modifying
    void deleteByCharacter(Character character);
}
