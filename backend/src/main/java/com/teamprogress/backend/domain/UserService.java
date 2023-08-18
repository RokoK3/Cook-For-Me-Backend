package com.teamprogress.backend.domain;

import com.teamprogress.backend.domain.model.CookForMeException;
import com.teamprogress.backend.persistance.entity.User;
import com.teamprogress.backend.persistance.entity.model.AccountStatus;
import com.teamprogress.backend.persistance.entity.model.Location;
import com.teamprogress.backend.persistance.repository.UserRepository;
import com.teamprogress.backend.rest.model.ErrorCode;
import com.teamprogress.backend.rest.model.requests.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new CookForMeException(ErrorCode.USER_DOESNT_EXIST));
    }

    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new CookForMeException(ErrorCode.USER_DOESNT_EXIST));
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public User patchUser(Long id, UserRequest userRequest) {
        var existingUser = getById(id);

        if(!existingUser.getUsername().equals(userRequest.getUsername()) && existsByUsername(userRequest.getUsername()))
            throw new CookForMeException(ErrorCode.USER_ALREADY_EXISTS);

        var user = User.builder()
                .id(id)
                .username(userRequest.getUsername())
                .email(existingUser.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .name(userRequest.getName())
                .lastname(userRequest.getLastname())
                .location(Location.fromValue(userRequest.getLocation()))
                .verificationCode(existingUser.getVerificationCode())
                .status(existingUser.getStatus())
                .isAdmin(existingUser.getIsAdmin())
                .orders(existingUser.getOrders())
                .build();

        return userRepository.save(user);
    }
    public boolean existsByUsernameOrEmail(String username, String email) {
        return userRepository.existsByUsernameOrEmail(username, email);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public boolean existsByUsernameAndActivated(String username) {
        return userRepository.existsByUsernameAndStatus(username, AccountStatus.ACTIVATED);
    }

}
