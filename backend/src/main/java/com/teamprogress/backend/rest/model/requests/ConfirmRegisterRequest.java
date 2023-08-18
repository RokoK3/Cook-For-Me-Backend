package com.teamprogress.backend.rest.model.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class ConfirmRegisterRequest {

    @NotNull
    @Size(min = 1, max = 20)
    String username;

    @NotNull
    @Size(min = 8)
    String password;

    @NotNull
    @Size(min = 36, max = 36)
    String activationCode;

}
