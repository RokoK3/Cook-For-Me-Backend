package com.teamprogress.backend.rest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DISH_ALREADY_EXISTS(400, 40001, "Dish already exists"),
    COOK_ALREADY_EXISTS(400, 40002, "Cook already exists"),
    USER_ALREADY_EXISTS(400, 40003, "User already exists"),
    VERIFICATION_CODE_INCORRECT(400, 40004, "Verification code is incorrect"),

    FORBIDDEN(403, 40301, "Not allowed"),

    COOK_DOESNT_EXIST(404, 40401, "Cook doesn't exists"),
    USER_DOESNT_EXIST(404, 40402, "User doesn't exists"),
    DISH_DOESNT_EXIST(404, 40403, "Dish doesn't exist"),
    ORDER_DOESNT_EXISTS(404, 40404, "Order doesn't exist");


    private final int httpStatus;
    private final int code;
    private final String message;

}
