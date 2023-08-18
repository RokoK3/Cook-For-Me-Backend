package com.teamprogress.backend.persistance.entity.converter;

import com.teamprogress.backend.persistance.entity.model.AccountStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class AccountStatusConverter implements AttributeConverter<AccountStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(AccountStatus accountStatus) {
        return accountStatus.getId();
    }

    @Override
    public AccountStatus convertToEntityAttribute(Integer id) {
        return AccountStatus.fromId(id);
    }
}
