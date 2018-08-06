package com.chaouki.tcshop.dao;

import com.chaouki.tcshop.entities.Character;
import com.chaouki.tcshop.entities.CharacterEquipment;
import com.chaouki.tcshop.entities.CharacterEquipmentSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CharacterEquipmentDao extends JpaRepository<CharacterEquipment, CharacterEquipmentSlot> {

    @Query("DELETE FROM CharacterEquipment WHERE slot.character = ?1")
    @Modifying
    void deleteByCharacter(Character character);

    @Query("SELECT equip FROM CharacterEquipment equip WHERE equip.slot.character = ?1")
    List<CharacterEquipment> findByCharacter(Character character);
}
