package com.teamprogress.backend.persistance.entity.converter;

import com.teamprogress.backend.persistance.entity.model.Ingredient;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class IngredientConverter implements AttributeConverter<Ingredient, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Ingredient ingredient) {
        return ingredient.getId();
    }

    @Override
    public Ingredient convertToEntityAttribute(Integer id) {
        return Ingredient.fromId(id);
    }
}
