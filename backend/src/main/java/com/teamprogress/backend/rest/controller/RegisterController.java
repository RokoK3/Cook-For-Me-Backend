package com.teamprogress.backend.rest.controller;

import com.teamprogress.backend.domain.RegisterService;
import com.teamprogress.backend.rest.model.requests.ConfirmRegisterRequest;
import com.teamprogress.backend.rest.model.requests.RegisterRequest;
import com.teamprogress.backend.rest.model.responses.UserDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegisterController {

    private final RegisterService registerService;

    @PostMapping("")
    UserDataResponse postRegister(@Valid @RequestBody RegisterRequest registerRequest) {
        return registerService.register(registerRequest);
    }

    @PostMapping("/confirm")
    UserDataResponse postRegisterConfirm(@RequestBody ConfirmRegisterRequest confirmRegisterRequest) {
        return registerService.activateAccount(confirmRegisterRequest);
    }

}