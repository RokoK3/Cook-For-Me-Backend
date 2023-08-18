package com.teamprogress.backend.rest.controller;

import com.teamprogress.backend.domain.DishService;
import com.teamprogress.backend.persistance.entity.model.Ingredient;
import com.teamprogress.backend.rest.model.requests.DishRequest;
import com.teamprogress.backend.rest.model.responses.DishResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/dish")
@Secured("ROLE_COOK")
public class DishController {

    private final DishService dishService;

    @GetMapping("/getInfo/{id}")
    @PreAuthorize("permitAll()")
    DishResponse getDishById(@PathVariable Long id) {
        return DishResponse.from(dishService.getById(id));
    }

    @GetMapping("/getAllDishes")
    @PreAuthorize("permitAll()")
    List<DishResponse> getAllDishes() {
        return dishService.getAllDishes().stream().map(DishResponse::from).toList();
    }

    @PostMapping("/create")
    DishResponse postCreateDish(@Valid @RequestBody DishRequest dishRequest) {
        return DishResponse.from(dishService.addDish(dishRequest));
    }

    @PatchMapping("/{id}")
    DishResponse patchDishById(@PathVariable Long id, @Valid @RequestBody DishRequest dishRequest) {
        return DishResponse.from(dishService.patch(id, dishRequest));
    }

    @DeleteMapping("/delete/{id}")
    boolean delete(@PathVariable Long id) {
        dishService.deleteById(id);
        return true;
    }

    @GetMapping("/getAllIngredients")
    @PreAuthorize("permitAll()")
    List<Ingredient> getAllIngredients() {
        return dishService.getAllIngredients();
    }

}

