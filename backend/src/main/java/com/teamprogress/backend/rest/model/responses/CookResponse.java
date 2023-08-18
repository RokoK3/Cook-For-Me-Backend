package com.teamprogress.backend.rest.model.responses;

import com.teamprogress.backend.persistance.entity.Cook;
import com.teamprogress.backend.persistance.entity.model.Cuisine;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CookResponse {

    private Long id;
    private String username;
    private String email;
    private String password;
    private String name;
    private String lastname;
    private String location;
    private BigDecimal rating;
    private Cuisine cuisine;
    private List<DishResponse> dishes = new LinkedList<>();

    public static CookResponse from(Cook cook) {
        return CookResponse.builder()
                .id(cook.getId())
                .username(cook.getUsername())
                .email(cook.getEmail())
                .password(cook.getPassword())
                .name(cook.getName())
                .lastname(cook.getLastname())
                .location(cook.getLocation().getValue())
                .rating(cook.getRating())
                .cuisine(cook.getCuisine())
                .dishes(cook.getDishes().stream().map(DishResponse::from).toList())
                .build();
    }
}
