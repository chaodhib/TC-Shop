package com.chaouki.tcshop.entities.converters;

import com.chaouki.tcshop.entities.enums.EquipmentSlot;
import com.chaouki.tcshop.entities.enums.ItemClass;

import javax.persistence.AttributeConverter;

public class EquipmentSlotConverter implements AttributeConverter<EquipmentSlot, Integer> {
    @Override
    public Integer convertToDatabaseColumn(EquipmentSlot attribute) {
        return attribute.getIdx();
    }

    @Override
    public EquipmentSlot convertToEntityAttribute(Integer dbData) {
        return EquipmentSlot.getByIndex(dbData);
    }
}
