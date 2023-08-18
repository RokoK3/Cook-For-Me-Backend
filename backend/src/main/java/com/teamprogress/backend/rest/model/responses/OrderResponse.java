package com.teamprogress.backend.rest.model.responses;

import com.teamprogress.backend.persistance.entity.UserOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long id;
    private Long userId;
    private Integer status;
    private List<DishResponse> dishes;

    public static OrderResponse from(UserOrder order) {
        var dishes = order.getOrderRating().stream().flatMap(orderRating -> {
            var dishResponse = DishResponse.from(orderRating.getDish());
            var listDish = new ArrayList<DishResponse>();

            for(int i = 0; i < orderRating.getAmount(); i++) {
                listDish.add(dishResponse);
            }

            return listDish.stream();
        }).toList();

        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .status(order.getStatus().getId())
                .dishes(dishes)
                .dishes(dishes)
                .build();
    }

}
