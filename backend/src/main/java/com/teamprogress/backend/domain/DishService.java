package com.teamprogress.backend.domain;

import com.teamprogress.backend.domain.model.CookForMeException;
import com.teamprogress.backend.persistance.entity.Dish;
import com.teamprogress.backend.persistance.entity.model.Ingredient;
import com.teamprogress.backend.persistance.repository.DishRepository;
import com.teamprogress.backend.rest.model.ErrorCode;
import com.teamprogress.backend.rest.model.requests.DishRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DishService {

    public final DishRepository dishRepository;
    public final CookService cookService;

    public Dish getById(Long id) {
        return dishRepository.findById(id).orElseThrow(() -> new CookForMeException(ErrorCode.DISH_DOESNT_EXIST));
    }

    public List<Dish> getAllDishes() {
        return dishRepository.findAll();
    }

    public List<Ingredient> getAllIngredients() {
        return Arrays.stream(Ingredient.values()).toList();
    }

    public Dish addDish(DishRequest dishRequest) {
        if(!cookService.existById(dishRequest.getCookId()))
            throw new CookForMeException(ErrorCode.COOK_DOESNT_EXIST);

        if(dishRepository.existsByCook_idAndName(dishRequest.getCookId(), dishRequest.getName()))
            throw new CookForMeException(ErrorCode.DISH_ALREADY_EXISTS);

        if(dishRequest.getMinOrders() > dishRequest.getMaxOrders())
            throw new CookForMeException(ErrorCode.FORBIDDEN);

        var dish = Dish.builder().name(dishRequest.getName())
                .minOrders(dishRequest.getMinOrders())
                .maxOrders(dishRequest.getMaxOrders())
                .image(dishRequest.getImage())
                .cook(cookService.getReferenceById(dishRequest.getCookId()))
                .preparationTime(dishRequest.getPreparationTime())
                .ingredients(dishRequest.getIngredients().stream().map(Ingredient::fromId).collect(Collectors.toSet()))
                .dishInfoPerDay(dishRequest.getDishInfoPerDay()
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(key -> DayOfWeek.of(key.getKey()), Map.Entry::getValue)))
                .build();

        return dishRepository.save(dish);
    }

    public void deleteById(Long id) {
        if(!dishRepository.existsById(id)) {
            throw new CookForMeException(ErrorCode.DISH_DOESNT_EXIST);
        }
        dishRepository.deleteById(id);
    }

    public Dish patch(Long id, DishRequest dishRequest) {
        var patchedDish = getById(id);

        patchedDish.setName(dishRequest.getName());
        patchedDish.setMinOrders(dishRequest.getMinOrders());
        patchedDish.setMaxOrders(dishRequest.getMaxOrders());
        patchedDish.setImage(dishRequest.getImage());
        patchedDish.setPreparationTime(dishRequest.getPreparationTime());
        patchedDish.setIngredients(dishRequest.getIngredients().stream().map(Ingredient::fromId).collect(Collectors.toSet()));
        patchedDish.setDishInfoPerDay(dishRequest.getDishInfoPerDay()
                .entrySet()
                .stream()
                .collect(Collectors.toConcurrentMap(key -> DayOfWeek.of(key.getKey()), Map.Entry::getValue)));

        return dishRepository.save(patchedDish);
    }

    public Dish getReferenceById(Long id) {
        return dishRepository.getReferenceById(id);
    }

}
