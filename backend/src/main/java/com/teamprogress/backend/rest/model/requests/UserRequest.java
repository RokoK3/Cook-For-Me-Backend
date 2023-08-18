package com.teamprogress.backend.rest.model.requests;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class UserRequest {

    @Size(min = 1, max = 20)
    String username;

    @Size(min = 8)
    String password;

    @Size(min = 1, max = 20)
    String name;

    @Size(min = 1, max = 20)
    String lastname;

    @Size(min = 1, max = 50)
    String location;

}
