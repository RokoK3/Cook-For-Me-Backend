package com.teamprogress.backend.rest.controller;

import com.teamprogress.backend.domain.CookService;
import com.teamprogress.backend.persistance.entity.model.Cuisine;
import com.teamprogress.backend.persistance.entity.model.Location;
import com.teamprogress.backend.rest.model.requests.CookRequest;
import com.teamprogress.backend.rest.model.responses.CookResponse;
import com.teamprogress.backend.rest.model.responses.DishResponse;
import com.teamprogress.backend.rest.model.responses.SelectItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cook")
@Secured("ROLE_COOK")
public class CookController {

    private final CookService cookService;

    @GetMapping("/getInfo/{id}")
    @PreAuthorize("permitAll()")
    CookResponse getCookById(@PathVariable Long id) {
        return CookResponse.from(cookService.getById(id));
    }

    @GetMapping("/getAllCooks")
    @PreAuthorize("permitAll()")
    List<CookResponse> getAllCooks() {
        return cookService.getAllCooks().stream().map(CookResponse::from).toList();
    }

    @GetMapping("/getDishes/{id}")
    @PreAuthorize("permitAll()")
    List<DishResponse> getDishes(@PathVariable Long id) {
        return cookService.getDishes(id).stream().map(DishResponse::from).toList();
    }

    @PatchMapping("/{id}")
    CookResponse patchCookById(@PathVariable Long id, @Valid @RequestBody CookRequest cookRequest) {
        return CookResponse.from(cookService.patchCook(id, cookRequest));
    }

    @GetMapping("/getCuisines")
    @PreAuthorize("permitAll()")
    List<SelectItemResponse> getCuisines() {
        return Arrays.stream(Cuisine.values()).map(SelectItemResponse::from).toList();
    }

    @GetMapping("/getLocations")
    @PreAuthorize("permitAll()")
    List<String> getLocations() {
        return Arrays.stream(Location.values()).map(Location::getValue).toList();
    }

}
