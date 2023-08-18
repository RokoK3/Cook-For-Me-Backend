package com.teamprogress.backend.rest.controller;

import com.teamprogress.backend.domain.UserService;
import com.teamprogress.backend.rest.model.requests.UserRequest;
import com.teamprogress.backend.rest.model.responses.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Secured("ROLE_USER")
public class UserController {

    private final UserService userService;

    @GetMapping("/getInfo/{id}")
    @PreAuthorize("permitAll()")
    UserResponse getUserById(@PathVariable Long id) {
        return UserResponse.from(userService.getById(id));
    }

    @PatchMapping("/{id}")
    UserResponse patchUserById(@PathVariable Long id, @Valid @RequestBody UserRequest userRequest) {
        return UserResponse.from(userService.patchUser(id, userRequest));
    }

}
