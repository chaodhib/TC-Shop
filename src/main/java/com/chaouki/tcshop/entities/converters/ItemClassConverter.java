package com.chaouki.tcshop.entities.converters;

import com.chaouki.tcshop.entities.enums.ItemClass;

import javax.persistence.AttributeConverter;

public class ItemClassConverter implements AttributeConverter<ItemClass, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ItemClass attribute) {
        return attribute.getIdx();
    }

    @Override
    public ItemClass convertToEntityAttribute(Integer dbData) {
        return ItemClass.getByIndex(dbData);
    }
}
