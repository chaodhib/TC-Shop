package com.chaouki.tcshop.entities.converters;

import com.chaouki.tcshop.entities.enums.InventoryType;

import javax.persistence.AttributeConverter;

public class InventoryTypeConverter implements AttributeConverter<InventoryType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(InventoryType attribute) {
        return attribute.getIdx();
    }

    @Override
    public InventoryType convertToEntityAttribute(Integer dbData) {
        return InventoryType.getByIndex(dbData);
    }
}
