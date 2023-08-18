package com.teamprogress.backend.domain;

import com.teamprogress.backend.domain.model.CookForMeException;
import com.teamprogress.backend.persistance.entity.*;
import com.teamprogress.backend.persistance.entity.model.OrderStatus;
import com.teamprogress.backend.persistance.repository.OrderRatingRepository;
import com.teamprogress.backend.persistance.repository.OrderRepository;
import com.teamprogress.backend.rest.model.requests.OrderRequest;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderRatingRepository orderRatingRepository;
    @Mock
    private DishService dishService;
    @Mock
    private CookService cookService;
    @Mock
    private UserService userService;
    @Mock
    private MailService mailService;

    @Test
    void shouldGetOrder() {
        // given
        var givenOrderId = 12L;
        var givenUserId = 3L;
        var givenOrderStatus = OrderStatus.IN_PROGRESS;
        var givenUserOrder = givenUserOrder(givenOrderId, givenUserId, givenOrderStatus);

        given(orderRepository.findById(givenOrderId)).willReturn(Optional.of(givenUserOrder));

        // when
        var actualOrder = orderService.getOrderById(givenOrderId);

        // then
        then(orderRepository).should().findById(givenOrderId);
        BDDAssertions.then(actualOrder.getId()).isEqualTo(givenOrderId);
        BDDAssertions.then(actualOrder.getUser().getId()).isEqualTo(givenUserId);
        BDDAssertions.then(actualOrder.getStatus()).isEqualTo(givenOrderStatus);
        BDDAssertions.then(actualOrder.getOrderRating()).isNull();
    }

    @Test
    void shouldThrow_whenNotFound() {
        // given
        var givenOrderId = 12L;

        given(orderRepository.findById(givenOrderId)).willReturn(Optional.empty());

        // when
        var thrown = catchThrowable(() -> orderService.getOrderById(givenOrderId));

        // then
        then(orderRepository).should().findById(givenOrderId);
        BDDAssertions.then(thrown).isInstanceOf(CookForMeException.class);
    }

    @Test
    void shouldGetAllUserOrders() {
        // given
        var givenUserId = 3L;
        var givenOrderId1 = 12L;
        var givenOrderId2 = 13L;
        var givenOrderStatus1 = OrderStatus.IN_PROGRESS;
        var givenOrderStatus2 = OrderStatus.COMPLETED;
        var givenUserOrder1 = givenUserOrder(givenOrderId1, givenUserId, givenOrderStatus1);
        var givenUserOrder2 = givenUserOrder(givenOrderId2, givenUserId, givenOrderStatus2);

        given(orderRepository.findAllByUser_Id(givenUserId)).willReturn(List.of(givenUserOrder1, givenUserOrder2));

        // when
        var actualOrders = orderService.getAllUserOrders(givenUserId);

        // then
        then(orderRepository).should().findAllByUser_Id(givenUserId);
        BDDAssertions.then(actualOrders.size()).isEqualTo(2);
        BDDAssertions.then(actualOrders.get(0).getStatus()).isEqualTo(OrderStatus.IN_PROGRESS);
        BDDAssertions.then(actualOrders.get(1).getStatus()).isEqualTo(OrderStatus.COMPLETED);
    }

    @Test
    void shouldSplitOrder() {
        // given
        var givenUserId = 12L;
        var givenUser = givenUser(givenUserId);
        var givenCookId1 = 13L;
        var givenCookId2 = 14L;
        var givenCook1 = givenCook(givenCookId1);
        var givenCook2 = givenCook(givenCookId2);
        var givenDishId1 = 1L;
        var givenDishId2 = 2L;
        var givenDish1 = givenDish(givenDishId1, givenCook1);
        var givenDish2 = givenDish(givenDishId2, givenCook2);
        var givenDishes = List.of(givenDishId1, givenDishId2);
        var givenOrderRequest = givenOrderRequest(givenUserId, givenDishes);
        var givenOrderId1 = 21L;
        var givenOrderId2 = 22L;
        var givenOrder1 = givenOrder(givenOrderId1);
        var givenOrder2 = givenOrder(givenOrderId2);

        given(dishService.getById(givenDishId1)).willReturn(givenDish1);
        given(dishService.getById(givenDishId2)).willReturn(givenDish2);
        given(userService.getById(givenUserId)).willReturn(givenUser);
        given(orderRepository.save(any(UserOrder.class))).willReturn(givenOrder1).willReturn(givenOrder2);

        // when
        var actualOrders = orderService.createOrder(givenOrderRequest);

        // then
        then(dishService).should(times(givenDishes.size())).getById(anyLong());
        then(userService).should().getById(givenUserId);
        then(orderRepository).should(times(givenDishes.size())).save(any(UserOrder.class));
        then(orderRatingRepository).should(times(2)).save(any(OrderRating.class));
        then(mailService).should(times(2)).sendMessage(any(), any(), any());
        BDDAssertions.then(actualOrders.size()).isEqualTo(2);
        BDDAssertions.then(actualOrders).containsAll(Set.of(givenOrderId1, givenOrderId2));
    }

    private UserOrder givenUserOrder(Long givenOrderId, Long givenUserId, OrderStatus givenOrderStatus) {
        return UserOrder.builder()
                .id(givenOrderId)
                .user(User.builder().id(givenUserId).build())
                .status(givenOrderStatus)
                .build();
    }

    private OrderRequest givenOrderRequest(Long givenUserId, List<Long> givenDishIds) {
        return new OrderRequest(givenUserId, givenDishIds);
    }

    private Dish givenDish(Long givenDishId, Cook givenCook) {
        return Dish.builder()
                .id(givenDishId)
                .cook(givenCook)
                .ingredients(Set.of())
                .build();
    }

    private Cook givenCook(Long givenCookId) {
        return Cook.builder()
                .id(givenCookId)
                .build();
    }

    private User givenUser(Long givenUserId) {
        return User.builder()
                .id(givenUserId)
                .build();
    }

    private UserOrder givenOrder(Long givenOrderId) {
        return UserOrder.builder()
                .id(givenOrderId)
                .build();
    }

}