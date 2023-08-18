package com.teamprogress.backend.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Value("${app.admin.password}")
    private String adminPass;
    private final UserService userService;
    private final CookService cookService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if("admin".equals(username)) { // Password: "pass"
            return User.builder()
                    .username(username)
                    .password(adminPass)
                    .authorities("ROLE_ADMIN", "ROLE_COOK", "ROLE_USER")
                    .build();
        } else if(cookService.existsByUsernameAndActivated(username)) {
            return User.builder()
                    .username(username)
                    .password(cookService.getByUsername(username).getPassword())
                    .authorities("ROLE_COOK")
                    .build();
        } else if(userService.existsByUsernameAndActivated(username)) {
            return User.builder()
                    .username(username)
                    .password(userService.getByUsername(username).getPassword())
                    .authorities("ROLE_USER")
                    .build();
        }
        throw new UsernameNotFoundException("No user " + username);
    }
}
