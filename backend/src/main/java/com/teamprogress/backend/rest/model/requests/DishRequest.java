package com.teamprogress.backend.rest.model.requests;

import com.teamprogress.backend.persistance.entity.DishDay;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;

@Data
public class DishRequest {

    @NotNull
    @Positive
    Long cookId;

    @NotNull
    @Size(min = 1, max = 20)
    String name;

    @NotNull
    @Size(min = 1, max = 1024)
    String image;

    @NotNull
    @PositiveOrZero
    Integer minOrders;

    @NotNull
    @Positive
    Integer maxOrders;

    @NotNull
    @Positive
    Integer preparationTime;

    @NotEmpty
    List<Integer> ingredients;

    @NotEmpty
    Map<Integer, DishDay> dishInfoPerDay;

}
