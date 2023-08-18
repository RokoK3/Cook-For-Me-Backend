package com.teamprogress.backend.persistance.entity.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum AccountStatus {

    UNVERIFIED(1),
    ACTIVATED(2),
    DISABLED(3);

    private final Integer id;

    public static AccountStatus fromId(Integer id) {
        return Arrays.stream(values()).filter(accountStatus -> accountStatus.getId().equals(id)).findAny().orElseThrow();
    }
}
