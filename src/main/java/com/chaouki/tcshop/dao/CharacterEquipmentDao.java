package com.chaouki.tcshop.dao;

import com.chaouki.tcshop.entities.CharacterEquipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterEquipmentDao extends JpaRepository<CharacterEquipment, Integer> {
}
