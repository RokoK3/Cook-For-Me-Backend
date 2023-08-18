package com.teamprogress.backend.persistance.entity.converter;

import com.teamprogress.backend.persistance.entity.model.Location;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class LocationConverter implements AttributeConverter<Location, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Location attribute) {
        return attribute.getId();
    }

    @Override
    public Location convertToEntityAttribute(Integer dbData) {
        return Location.fromId(dbData);
    }
}
