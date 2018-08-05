package com.chaouki.tcshop.entities.converters;

import com.chaouki.tcshop.entities.ItemQuality;

import javax.persistence.AttributeConverter;

public class ItemQualityConverter implements AttributeConverter<ItemQuality, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ItemQuality attribute) {
        return attribute.getIdx();
    }

    @Override
    public ItemQuality convertToEntityAttribute(Integer dbData) {
        return ItemQuality.getByIndex(dbData);
    }
}
