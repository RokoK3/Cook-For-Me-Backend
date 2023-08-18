package com.teamprogress.backend.rest.model.responses;


import com.teamprogress.backend.persistance.entity.Cook;
import com.teamprogress.backend.persistance.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDataResponse {
    private Long id;
    private String username;
    private String password;
    private String role;

    public static UserDataResponse from(User user) {
        return UserDataResponse.builder()
                .id(user.getId())
                .role("USER")
                .password(user.getPassword())
                .username(user.getUsername())
                .build();
    }

    public static UserDataResponse from(Cook user) {
        return UserDataResponse.builder()
                .id(user.getId())
                .role("COOK")
                .password(user.getPassword())
                .username(user.getUsername())
                .build();
    }

}
