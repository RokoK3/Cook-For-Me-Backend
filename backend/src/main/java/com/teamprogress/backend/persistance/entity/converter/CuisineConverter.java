package com.teamprogress.backend.persistance.entity.converter;

import com.teamprogress.backend.persistance.entity.model.Cuisine;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class CuisineConverter implements AttributeConverter<Cuisine, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Cuisine cuisine) {
        return cuisine.getId();
    }

    @Override
    public Cuisine convertToEntityAttribute(Integer id) {
        return Cuisine.fromId(id);
    }
}
