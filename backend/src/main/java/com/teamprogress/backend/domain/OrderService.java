package com.teamprogress.backend.domain;

import com.teamprogress.backend.domain.model.CookForMeException;
import com.teamprogress.backend.persistance.entity.*;
import com.teamprogress.backend.persistance.entity.model.OrderStatus;
import com.teamprogress.backend.persistance.repository.OrderRatingRepository;
import com.teamprogress.backend.persistance.repository.OrderRepository;
import com.teamprogress.backend.rest.model.ErrorCode;
import com.teamprogress.backend.rest.model.requests.OrderRequest;
import com.teamprogress.backend.rest.model.requests.RateDishRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderRatingRepository orderRatingRepository;
    private final DishService dishService;
    private final CookService cookService;
    private final UserService userService;
    private final MailService mailService;

    public UserOrder getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new CookForMeException(ErrorCode.ORDER_DOESNT_EXISTS));
    }

    public List<UserOrder> getAllUserOrders(Long userId) {
        return orderRepository.findAllByUser_Id(userId);
    }

    public List<UserOrder> getAllCookOrders(Long cookId) {
        var cook = cookService.getById(cookId);
        var userOrders = cook.getDishes().stream()
                .flatMap(dish -> dish.getOrders().stream())
                .map(OrderRating::getUserOrder)
                .collect(Collectors.toSet());

        return userOrders.stream().peek(userOrder -> userOrder.setOrderRating(
                userOrder.getOrderRating().stream()
                        .filter(orderRating -> orderRating.getDish().getCook().getId().equals(cookId))
                        .collect(Collectors.toSet()))
        ).toList();
    }

    @Transactional
    public List<Long> createOrder(OrderRequest orderRequest) {
        //Map<CookId, Map<dishId, amount>>
        var mapOfDishes = new HashMap<Long, Map<Long, Integer>>();
        var dishes = orderRequest.getDishes().stream().map(dishService::getById).toList();
        var user = userService.getById(orderRequest.getUserId());

        dishes.forEach(dish -> {
            var cookId = dish.getCook().getId();
            var dishId = dish.getId();

            mapOfDishes.putIfAbsent(cookId, new HashMap<>());
            mapOfDishes.get(cookId).computeIfAbsent(dishId, v -> 0);
            mapOfDishes.get(cookId).computeIfPresent(dishId, (key, value) -> value + 1);
        });

        var orders = mapOfDishes.values().stream().map(dishAmount -> {
            var userOrder = UserOrder.builder().user(user).status(OrderStatus.IN_PROGRESS).build();
            var orderId = orderRepository.save(userOrder).getId();

            dishAmount.forEach((dishId, amount) -> orderRatingRepository.save(OrderRating.builder()
                    .id(new OrderRatingKey(orderId, dishId))
                    .dish(dishService.getReferenceById(dishId))
                    .userOrder(orderRepository.getReferenceById(orderId))
                    .amount(amount)
                    .build()));

            return orderId;
        }).toList();

        var distinctDishes = dishes.stream().distinct().toList();
        new Thread(() -> notifyUserCreated(user.getEmail(), distinctDishes)).start();
        new Thread(() -> notifyCooksCreated(distinctDishes, user)).start();

        return orders;
    }

    public void changeOrderStatus(Long orderId, OrderStatus orderStatus) {
        var orderReference = orderRepository.findById(orderId).orElseThrow(() -> new CookForMeException(ErrorCode.ORDER_DOESNT_EXISTS));
        orderReference.setStatus(orderStatus);

        var order = orderRepository.save(orderReference);

        new Thread(() -> notifyUserChanged(order)).start();
    }

    public void rateDish(RateDishRequest rateDishRequest) {
        var orderRating = orderRatingRepository.findById(new OrderRatingKey(rateDishRequest.getOrderId(), rateDishRequest.getDishId()))
                .orElseThrow(() -> new CookForMeException(ErrorCode.ORDER_DOESNT_EXISTS));

        orderRating.setDishRating(rateDishRequest.getRating());
        orderRatingRepository.save(orderRating);

        new Thread(() -> updateCookRatingByDishId(rateDishRequest.getDishId())).start();
    }

    public List<OrderRating> getAllOrderedDishesInProgress() {
        return orderRepository.findAllByStatus(OrderStatus.IN_PROGRESS)
                .stream()
                .flatMap(userOrder -> userOrder.getOrderRating().stream())
                .toList();
    }

    public List<UserOrder> getAllOrdersInProgress() {
        return orderRepository.findAllByStatus(OrderStatus.IN_PROGRESS);
    }

    public Integer remainingDishOrders(Long dishId) {
        var dish = dishService.getById(dishId);
        var ordered = getAllOrderedDishesInProgress()
                .stream()
                .filter(orderRating -> orderRating.getId().getDishId().equals(dishId))
                .mapToInt(OrderRating::getAmount)
                .sum();
        return dish.getMaxOrders() - ordered;
    }

    private void notifyUserCreated(String email, List<Dish> dishes) {
        var message = new StringBuilder("You have successfully ordered following items:\n\n");
        dishes.forEach(dish -> {
            message.append("\t%s:\n".formatted(dish.getName()));
            dish.getIngredients().forEach(ingredient -> message.append("\t\t%s\n".formatted(ingredient.name())));
            message.append("\n");
        });
        message.append("Sincerely, Cook For Me team!");

        mailService.sendMessage(email, "Order confirmation", message.toString());
    }

    private void notifyCooksCreated(List<Dish> dishes, User user) {
        var mapOfDishes = new HashMap<String, List<String>>();

        dishes.forEach(dish -> {
            var mail = dish.getCook().getEmail();

            mapOfDishes.putIfAbsent(mail, new ArrayList<>());
            mapOfDishes.get(mail).add(dish.getName());
        });

        mapOfDishes.forEach((mail, list) -> {
            var message = new StringBuilder("You have a new order by user %s with the following item/s:\n\n".formatted(user.getUsername()));
            list.forEach(dishName -> message.append("\t%s\n".formatted(dishName)));
            message.append("\nPlease deliver the order to %s.\n\nSincerely, Cook For Me team!".formatted(user.getLocation()));

            mailService.sendMessage(mail, "New order", message.toString());
        });
    }

    private void notifyUserChanged(UserOrder userOrder) {
        var email = userOrder.getUser().getEmail();
        var orderStatus = userOrder.getStatus();
        var orderId = userOrder.getId();

        var message = new StringBuilder("\n\nSincerely, Cook For Me team!");
        var title = new StringBuilder("Order ");

        if(orderStatus == OrderStatus.COMPLETED) {
            var messageToInsert = """
                    Your order (id: %s) was successfully completed!
                                        
                    Please rate your order on the following link: https://cook-for-me.onrender.com/order/%s/rate
                    """.trim().formatted(orderId, orderId);
            message.insert(0, messageToInsert);
            title.append("completed");
        } else if(orderStatus == OrderStatus.DECLINED) {
            message.insert(0, "We are sorry to inform you that your order (id: %s) was declined!".formatted(orderId));
            title.append("declined");
        } else {
            throw new CookForMeException(ErrorCode.FORBIDDEN);
        }

        title.append(" - %s".formatted(orderId));
        mailService.sendMessage(email, title.toString(), message.toString());
    }

    private void updateCookRatingByDishId(Long dishId) {
        var cook = dishService.getById(dishId).getCook();

        var rating = cook.getDishes().stream()
                .flatMap(dish -> dish.getOrders().stream())
                .filter(orderRating -> orderRating.getDishRating() != null)
                .mapToDouble(OrderRating::getDishRating)
                .average()
                .orElse(0);

        cook.setRating(BigDecimal.valueOf(rating));

        cookService.save(cook);
    }

}
