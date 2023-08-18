package com.teamprogress.backend.persistance.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.DayOfWeek;

@Converter
public class DayOfWeekConverter implements AttributeConverter<DayOfWeek, Integer> {

    @Override
    public Integer convertToDatabaseColumn(DayOfWeek dayOfWeek) {
        return dayOfWeek.getValue();
    }

    @Override
    public DayOfWeek convertToEntityAttribute(Integer id) {
        return DayOfWeek.of(id);
    }
}
