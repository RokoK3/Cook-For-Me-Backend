package com.teamprogress.backend.domain;

import com.teamprogress.backend.domain.model.CookForMeException;
import com.teamprogress.backend.persistance.entity.Cook;
import com.teamprogress.backend.persistance.entity.Dish;
import com.teamprogress.backend.persistance.entity.model.AccountStatus;
import com.teamprogress.backend.persistance.entity.model.Location;
import com.teamprogress.backend.persistance.repository.CookRepository;
import com.teamprogress.backend.rest.model.ErrorCode;
import com.teamprogress.backend.rest.model.requests.CookRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CookService {

    private final CookRepository cookRepository;
    private final PasswordEncoder passwordEncoder;

    public Cook getByUsername(String username) {
        return cookRepository.findByUsername(username).orElseThrow(() -> new CookForMeException(ErrorCode.COOK_DOESNT_EXIST));
    }

    public Cook getById(Long id) {
        return cookRepository.findById(id).orElseThrow(() -> new CookForMeException(ErrorCode.COOK_DOESNT_EXIST));
    }

    public Set<Dish> getDishes(Long id) {
        var cook = cookRepository.findById(id).orElseThrow(() -> new CookForMeException(ErrorCode.COOK_DOESNT_EXIST));
        return cook.getDishes();
    }

    public List<Cook> getAllCooks() {
        return cookRepository.findAll().stream().filter(cook -> cook.getStatus().equals(AccountStatus.ACTIVATED)).toList();
    }

    public boolean existsByUsername(String username) {
        return cookRepository.existsByUsername(username);
    }

    public Cook patchCook(Long id, CookRequest cookRequest) {
        var existingCook = getById(id);

        if(!existingCook.getUsername().equals(cookRequest.getUsername()) && existsByUsername(cookRequest.getUsername()))
            throw new CookForMeException(ErrorCode.COOK_ALREADY_EXISTS);

        var cook = Cook.builder()
                .id(id)
                .name(cookRequest.getName())
                .lastname(cookRequest.getLastname())
                .username(cookRequest.getUsername())
                .password(passwordEncoder.encode(cookRequest.getPassword()))
                .location(Location.fromValue(cookRequest.getLocation()))
                .verificationCode(existingCook.getVerificationCode())
                .status(existingCook.getStatus())
                .email(existingCook.getEmail())
                .cuisine(existingCook.getCuisine())
                .dishes(existingCook.getDishes())
                .rating(existingCook.getRating())
                .build();
        return cookRepository.save(cook);
    }

    public boolean existById(Long id) {
        return cookRepository.existsById(id);
    }

    public Cook getReferenceById(Long id) {
        return cookRepository.getReferenceById(id);
    }

    public boolean existsByUsernameOrEmail(String username, String email) {
        return cookRepository.existsByUsernameOrEmail(username, email);
    }

    public Cook save(Cook cook) {
        return cookRepository.save(cook);
    }

    public boolean existsByUsernameAndActivated(String username) {
        return cookRepository.existsByUsernameAndStatus(username, AccountStatus.ACTIVATED);
    }

}
