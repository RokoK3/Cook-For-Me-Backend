package com.teamprogress.backend.persistance.entity.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Cuisine {

    FRENCH(1),
    ITALIAN(2),
    ASIAN(3);

    private final Integer id;

    public static Cuisine fromId(Integer id) {
        return Arrays.stream(values()).filter(cuisine -> cuisine.getId().equals(id)).findAny().orElseThrow();
    }
}
