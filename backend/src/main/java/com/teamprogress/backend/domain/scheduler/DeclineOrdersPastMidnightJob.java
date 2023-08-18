package com.teamprogress.backend.domain.scheduler;

import com.teamprogress.backend.domain.OrderService;
import com.teamprogress.backend.persistance.entity.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeclineOrdersPastMidnightJob {

    private final OrderService orderService;

    @Async
    @Scheduled(cron = "0 29 0 * * *", zone = "GMT+1:00") // midnight + 29 minutes
    void job() {
        log.info("Check if any orders have expired.");
        var activeOrders = orderService.getAllOrdersInProgress();

        activeOrders.forEach(order -> {
            log.info("Declining order {}.", order.getId());
            orderService.changeOrderStatus(order.getId(), OrderStatus.DECLINED);
        });
    }
}
