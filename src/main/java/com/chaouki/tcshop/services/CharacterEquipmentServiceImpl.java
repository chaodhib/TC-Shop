package com.chaouki.tcshop.services;

import com.chaouki.tcshop.dao.CharacterEquipmentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class CharacterEquipmentServiceImpl implements CharacterEquipmentService {

    private final CharacterEquipmentDao characterEquipmentDao;

    @Autowired
    public CharacterEquipmentServiceImpl(CharacterEquipmentDao characterEquipmentDao) {
        this.characterEquipmentDao = characterEquipmentDao;
    }

    @Override
    public void updateEquipment(Integer characterId, LocalDateTime timestamp, Map<Integer, Integer> itemMap) {
        System.out.println();
    }
}
