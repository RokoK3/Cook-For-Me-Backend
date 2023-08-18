package com.teamprogress.backend.persistance.entity.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Location {

    VRBOVEC(1, "Vrbovec"),
    ZAGREB(2, "Zagreb"),
    DUGO_SELO(3, "Dugo Selo"),
    VELIKA_GORICA(4, "Velika Gorica");

    private final Integer id;
    private final String value;

    public static Location fromId(Integer id) {
        return Arrays.stream(values()).filter(location -> location.getId().equals(id)).findAny().orElseThrow();
    }

    public static Location fromValue(String value) {
        return Arrays.stream(values()).filter(location -> location.getValue().equalsIgnoreCase(value)).findAny().orElseThrow();
    }

}
