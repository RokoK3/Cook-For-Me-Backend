package com.teamprogress.backend.rest.model.responses;

import com.teamprogress.backend.persistance.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private String password;
    private String name;
    private String lastname;
    private String location;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .name(user.getName())
                .lastname(user.getLastname())
                .location(user.getLocation().getValue())
                .build();
    }

}
