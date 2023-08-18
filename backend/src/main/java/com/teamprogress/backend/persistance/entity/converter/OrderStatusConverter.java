package com.teamprogress.backend.persistance.entity.converter;

import com.teamprogress.backend.persistance.entity.model.OrderStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class OrderStatusConverter implements AttributeConverter<OrderStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(OrderStatus orderStatus) {
        return orderStatus.getId();
    }

    @Override
    public OrderStatus convertToEntityAttribute(Integer id) {
        return OrderStatus.fromId(id);
    }
}
