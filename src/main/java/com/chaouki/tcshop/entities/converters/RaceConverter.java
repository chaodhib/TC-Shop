package com.chaouki.tcshop.entities.converters;

import com.chaouki.tcshop.entities.Race;

import javax.persistence.AttributeConverter;

/**
 * Created by chaouki on 20/03/2018.
 */
public class RaceConverter implements AttributeConverter<Race, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Race attribute) {
        return attribute.getIdx();
    }

    @Override
    public Race convertToEntityAttribute(Integer dbData) {
        return Race.getByIndex(dbData);
    }
}
