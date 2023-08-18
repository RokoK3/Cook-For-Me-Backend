package com.teamprogress.backend.rest.model.requests;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Validated
public class RateDishRequest {

    @NotNull
    @Positive
    Long orderId;

    @NotNull
    @Positive
    Long dishId;

    @NotNull
    @Range(min = 1, max = 5)
    Integer rating;
}
