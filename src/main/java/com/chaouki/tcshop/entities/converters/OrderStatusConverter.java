package com.chaouki.tcshop.entities.converters;

import com.chaouki.tcshop.entities.enums.OrderStatus;

import javax.persistence.AttributeConverter;

public class OrderStatusConverter implements AttributeConverter<OrderStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(OrderStatus attribute) {
        return attribute.getIdx();
    }

    @Override
    public OrderStatus convertToEntityAttribute(Integer dbData) {
        return OrderStatus.getByIndex(dbData);
    }
}
