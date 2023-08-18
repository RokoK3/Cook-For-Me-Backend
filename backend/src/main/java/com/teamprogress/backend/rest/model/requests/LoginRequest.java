package com.teamprogress.backend.rest.model.requests;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class LoginRequest {

    @NotNull
    @Size(min = 1, max = 20)
    String username;

    @NotNull
    @Size(min = 8)
    String password;

}
