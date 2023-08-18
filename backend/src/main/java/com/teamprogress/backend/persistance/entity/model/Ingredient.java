package com.teamprogress.backend.persistance.entity.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Ingredient {

    TOMATO(1),
    MIlK(2),
    BEEF(3),
    CHICKEN(4),
    SALT(5),
    SUGAR(6),
    ONION(7),
    GREEN_SALAD(8),
    FLOUR(9),
    CUCUMBER(10);

    private final Integer id;

    public static Ingredient fromId(Integer id) {
        return Arrays.stream(values()).filter(ingredient -> ingredient.getId().equals(id)).findAny().orElseThrow();
    }

}