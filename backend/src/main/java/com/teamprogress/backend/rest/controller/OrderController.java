package com.teamprogress.backend.rest.controller;

import com.teamprogress.backend.domain.OrderService;
import com.teamprogress.backend.persistance.entity.model.OrderStatus;
import com.teamprogress.backend.persistance.repository.OrderRatingRepository;
import com.teamprogress.backend.rest.model.requests.OrderRequest;
import com.teamprogress.backend.rest.model.requests.RateDishRequest;
import com.teamprogress.backend.rest.model.responses.OrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderRatingRepository orderRatingRepository;

    private final OrderService orderService;

    @GetMapping("/getInfo/{id}")
    OrderResponse getOrderById(@PathVariable Long id) {
        return OrderResponse.from(orderService.getOrderById(id));
    }

    @Secured("ROLE_USER")
    @GetMapping("/getAllUserOrders/{userId}")
    List<OrderResponse> getAllUserOrders(@PathVariable Long userId) {
        return orderService.getAllUserOrders(userId).stream().map(OrderResponse::from).toList();
    }

    @Secured("ROLE_COOK")
    @GetMapping("/getAllCookOrders/{cookId}")
    List<OrderResponse> getAllCookOrders(@PathVariable Long cookId) {
        return orderService.getAllCookOrders(cookId).stream().map(OrderResponse::from).toList();
    }

    @Secured("ROLE_USER")
    @PostMapping("/create")
    List<Long> postOrder(@Valid @RequestBody OrderRequest orderRequest) {
        return orderService.createOrder(orderRequest);
    }

    @Secured("ROLE_COOK")
    @PatchMapping("/{orderId}/{orderStatusId}")
    void patchOrderStatus(@PathVariable Long orderId, @Valid @PathVariable Integer orderStatusId) {
        orderService.changeOrderStatus(orderId, OrderStatus.fromId(orderStatusId));
    }

    @Secured("ROLE_USER")
    @PostMapping("/rate")
    void postRate(@Valid @RequestBody RateDishRequest rateDishRequest) {
        orderService.rateDish(rateDishRequest);
    }

    @Secured("ROLE_USER")
    @GetMapping("/orderAmountRemaining/{dishId}")
    Integer postOrderAmountRemaining(@PathVariable Long dishId) {
        return orderService.remainingDishOrders(dishId);
    }

}
