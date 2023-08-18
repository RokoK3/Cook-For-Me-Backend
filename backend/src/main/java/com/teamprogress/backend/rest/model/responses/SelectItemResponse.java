package com.teamprogress.backend.rest.model.responses;

import com.teamprogress.backend.persistance.entity.model.Cuisine;
import com.teamprogress.backend.persistance.entity.model.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SelectItemResponse {

    String name;
    String value;

    public static SelectItemResponse from(Ingredient ingredient) {
        return SelectItemResponse.builder()
                .name(ingredient.name())
                .value(ingredient.getId().toString())
                .build();
    }

    public static SelectItemResponse from(Cuisine cuisine) {
        return SelectItemResponse.builder()
                .name(cuisine.name())
                .value(cuisine.getId().toString())
                .build();
    }

}
