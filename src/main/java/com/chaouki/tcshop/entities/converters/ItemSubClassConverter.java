package com.chaouki.tcshop.entities.converters;

import com.chaouki.tcshop.entities.InventoryType;
import com.chaouki.tcshop.entities.ItemSubClass;

import javax.persistence.AttributeConverter;

public class ItemSubClassConverter implements AttributeConverter<ItemSubClass, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ItemSubClass attribute) {
        return attribute.getIdx();
    }

    @Override
    public ItemSubClass convertToEntityAttribute(Integer dbData) {
        return ItemSubClass.getByIndex(dbData);
    }
}
