package com.teamprogress.backend.rest.model.requests;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
public class RegisterRequest {

    @NotNull
    @Size(min = 1, max = 20)
    String username;

    @Email
    @NotNull
    @Size(max = 50)
    String email;

    @NotNull
    @Size(min = 1, max = 20)
    String name;

    @NotNull
    @Size(min = 1, max = 20)
    String lastname;

    @NotNull
    @Size(min = 1, max = 50)
    String location;

    @NotNull
    Boolean isCook;

    @Positive
    Integer cuisineId;

}
