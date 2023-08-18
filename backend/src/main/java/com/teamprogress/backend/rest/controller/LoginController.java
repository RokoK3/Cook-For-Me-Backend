package com.teamprogress.backend.rest.controller;

import com.teamprogress.backend.domain.CookService;
import com.teamprogress.backend.domain.UserService;
import com.teamprogress.backend.domain.model.CookForMeException;
import com.teamprogress.backend.rest.model.ErrorCode;
import com.teamprogress.backend.rest.model.responses.UserDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final UserService userService;
    private final CookService cookService;

    @GetMapping("")
    UserDataResponse getLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.isAuthenticated() && authentication.getAuthorities().size() > 0) {
            throw new CookForMeException(ErrorCode.FORBIDDEN);
        }
        String username = authentication.getName();

        if(userService.existsByUsernameAndActivated(username)) {
            return UserDataResponse.from(userService.getByUsername(username));
        } else if(cookService.existsByUsernameAndActivated(username)) {
            return UserDataResponse.from(cookService.getByUsername(username));
        } else throw new CookForMeException(ErrorCode.USER_DOESNT_EXIST);
    }
}