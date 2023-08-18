package com.teamprogress.backend.domain.scheduler;

import com.teamprogress.backend.domain.DishService;
import com.teamprogress.backend.domain.OrderService;
import com.teamprogress.backend.persistance.entity.OrderRatingKey;
import com.teamprogress.backend.persistance.repository.OrderRatingRepository;
import com.teamprogress.backend.persistance.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.internal.util.Pair;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RemoveSmallOrdersJob {

    private final OrderService orderService;
    private final DishService dishService;
    private final OrderRatingRepository orderRatingRepository;
    private final OrderRepository orderRepository;

    @Async
    @Scheduled(cron = "5 * * * * *") // minute + 5 seconds
    public void job() {
        log.info("Check if any orders haven't met the minimum order requirement.");
        var orderRatings = orderService.getAllOrderedDishesInProgress();

        // Map<orderId, Pair<List<orderId>, amount>>
        var map = new HashMap<Long, Pair<List<Long>, Integer>>();
        orderRatings.forEach(order -> {
            var orderKey = order.getId();
            var dishId = orderKey.getDishId();
            var orderId = orderKey.getOrderId();

            map.putIfAbsent(dishId, Pair.of(List.of(), 0));

            var value = map.get(dishId);
            var list = new java.util.ArrayList<>(value.getLeft());
            list.add(orderId);

            map.put(dishId, Pair.of(list, value.getRight() + order.getAmount()));
        });

        map.forEach((dishId, pair) -> {
            var dish = dishService.getById(dishId);
            var amount = pair.getRight();
            var dishInfoPerDay = dish.getDishInfoPerDay();

            if(dishInfoPerDay != null){
                log.info("DOW: {}",LocalDateTime.now().getDayOfWeek());
                var preparationTimePeriod = dishInfoPerDay.get(LocalDateTime.now().getDayOfWeek());
                var timeToOrderPassed = preparationTimePeriod.getEndTime().getHours() <=(Time.from(Instant.now()).getHours() + 1)
                        && preparationTimePeriod.getEndTime().getMinutes() <= Time.from(Instant.now()).getMinutes();
                log.info("Order Hours {}, Minutes {}", preparationTimePeriod.getEndTime().getHours() + 1, preparationTimePeriod.getEndTime().getMinutes());
                log.info("Instant Hours {}, Minutes {}", Time.from(Instant.now()).getHours(), Time.from(Instant.now()).getMinutes());

                if(timeToOrderPassed && dish.getMinOrders() > amount) {
                    log.info("Removing dish(id: {}) - too little orders.", dishId);
                    var list = pair.getLeft();
                    list.forEach(orderId -> orderRatingRepository.deleteById(new OrderRatingKey(orderId, dishId)));
                }
            }
        });

        var inProgress = orderService.getAllOrdersInProgress();
        inProgress.forEach(userOrder -> {
            if(userOrder.getOrderRating().isEmpty()) orderRepository.deleteById(userOrder.getId());
        });
    }

}
