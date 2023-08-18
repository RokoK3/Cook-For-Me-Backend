package com.teamprogress.backend.persistance.entity.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum OrderStatus {

    IN_PROGRESS(1),
    COMPLETED(2),
    DECLINED(3);

    private final Integer id;

    public static OrderStatus fromId(Integer id) {
        return Arrays.stream(values()).filter(status -> status.getId().equals(id)).findAny().orElseThrow();
    }

}
