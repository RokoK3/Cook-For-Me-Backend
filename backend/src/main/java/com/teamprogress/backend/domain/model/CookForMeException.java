package com.teamprogress.backend.domain.model;

import com.teamprogress.backend.rest.model.ErrorCode;
import lombok.Getter;

@Getter
public class CookForMeException extends RuntimeException {

    private final ErrorCode errorCode;

    public CookForMeException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
