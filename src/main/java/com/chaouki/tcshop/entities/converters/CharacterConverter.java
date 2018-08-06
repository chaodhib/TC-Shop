package com.chaouki.tcshop.entities.converters;

import com.chaouki.tcshop.entities.enums.CharacterClass;

import javax.persistence.AttributeConverter;

/**
 * Created by chaouki on 20/03/2018.
 */
public class CharacterConverter implements AttributeConverter<CharacterClass, Integer> {
    @Override
    public Integer convertToDatabaseColumn(CharacterClass attribute) {
        return attribute.getIdx();
    }

    @Override
    public CharacterClass convertToEntityAttribute(Integer dbData) {
        return CharacterClass.getByIndex(dbData);
    }
}
