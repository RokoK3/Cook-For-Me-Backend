package com.teamprogress.backend.rest.model.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderRequest {

    @NotNull
    @Positive
    Long userId;

    @NotEmpty
    List<Long> dishes;
}

