package com.teamprogress.backend.rest.model.responses;

import com.teamprogress.backend.persistance.entity.Dish;
import com.teamprogress.backend.persistance.entity.DishDay;
import com.teamprogress.backend.persistance.entity.model.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishResponse {

    private Long id;
    private Long cookId;
    private String name;
    private String image;
    private Integer minOrders;
    private Integer maxOrders;
    private Integer preparationTime;
    private Set<Ingredient> ingredients = new HashSet<>();
    private Map<DayOfWeek, DishDay> dishInfoPerDay = new HashMap<>();

    public static DishResponse from(Dish dish) {
        return DishResponse.builder()
                .id(dish.getId())
                .cookId(dish.getCook().getId())
                .name(dish.getName())
                .image(dish.getImage())
                .minOrders(dish.getMinOrders())
                .maxOrders(dish.getMaxOrders())
                .preparationTime(dish.getPreparationTime())
                .ingredients(dish.getIngredients())
                .dishInfoPerDay(dish.getDishInfoPerDay())
                .build();
    }
}
