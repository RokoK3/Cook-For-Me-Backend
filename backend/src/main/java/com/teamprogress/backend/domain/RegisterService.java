package com.teamprogress.backend.domain;

import com.teamprogress.backend.domain.model.CookForMeException;
import com.teamprogress.backend.persistance.entity.Cook;
import com.teamprogress.backend.persistance.entity.User;
import com.teamprogress.backend.persistance.entity.model.AccountStatus;
import com.teamprogress.backend.persistance.entity.model.Cuisine;
import com.teamprogress.backend.persistance.entity.model.Location;
import com.teamprogress.backend.rest.model.ErrorCode;
import com.teamprogress.backend.rest.model.requests.ConfirmRegisterRequest;
import com.teamprogress.backend.rest.model.requests.RegisterRequest;
import com.teamprogress.backend.rest.model.responses.UserDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final UserService userService;
    private final CookService cookService;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    public UserDataResponse register(RegisterRequest registerRequest) {
        UserDataResponse userDataResponse;
        String email;

        var code = getCode();

        if(registerRequest.getIsCook()) {
            var cook = registerCook(registerRequest, code);
            userDataResponse = UserDataResponse.from(cook);
            email = cook.getEmail();
        } else {
            var user = registerUser(registerRequest, code);
            userDataResponse = UserDataResponse.from(user);
            email = user.getEmail();
        }

        new Thread(() -> sendActivationEmail(email, code)).start();

        return userDataResponse;
    }

    public UserDataResponse activateAccount(ConfirmRegisterRequest confirmRegisterRequest) {
        var username = confirmRegisterRequest.getUsername();
        if(cookService.existsByUsername(username) && !cookService.existsByUsernameAndActivated(username)) {
            var cook = cookService.getByUsername(username);
            checkCode(cook.getVerificationCode(), confirmRegisterRequest.getActivationCode());

            cook.setPassword(passwordEncoder.encode(confirmRegisterRequest.getPassword()));
            cook.setStatus(AccountStatus.ACTIVATED);

            return UserDataResponse.from(cookService.save(cook));
        } else if(userService.existsByUsername(username) && !userService.existsByUsernameAndActivated(username)) {
            var user = userService.getByUsername(username);
            checkCode(user.getVerificationCode(), confirmRegisterRequest.getActivationCode());

            user.setPassword(passwordEncoder.encode(confirmRegisterRequest.getPassword()));
            user.setStatus(AccountStatus.ACTIVATED);

            return UserDataResponse.from(userService.save(user));
        } else {
            throw new CookForMeException(ErrorCode.FORBIDDEN);
        }
    }

    private Cook registerCook(RegisterRequest registerRequest, String code) {
        var cook = Cook.builder()
                .status(AccountStatus.UNVERIFIED)
                .email(registerRequest.getEmail())
                .username(registerRequest.getUsername())
                .name(registerRequest.getName())
                .lastname(registerRequest.getLastname())
                .location(Location.fromValue(registerRequest.getLocation()))
                .verificationCode(code)
                .cuisine(Cuisine.fromId(registerRequest.getCuisineId()))
                .rating(BigDecimal.ZERO)
                .build();

        if(cookService.existsByUsernameOrEmail(cook.getUsername(), cook.getEmail())) {
            throw new CookForMeException(ErrorCode.COOK_ALREADY_EXISTS);
        }

        return cookService.save(cook);
    }

    private User registerUser(RegisterRequest registerRequest, String code) {
        var user = User.builder()
                .status(AccountStatus.UNVERIFIED)
                .email(registerRequest.getEmail())
                .username(registerRequest.getUsername())
                .name(registerRequest.getName())
                .lastname(registerRequest.getLastname())
                .verificationCode(code)
                .location(Location.fromValue(registerRequest.getLocation()))
                .isAdmin(false)
                .build();

        if(userService.existsByUsernameOrEmail(user.getUsername(), user.getEmail())) {
            throw new CookForMeException(ErrorCode.USER_ALREADY_EXISTS);
        }

        return userService.save(user);
    }

    private String getCode() {
        return UUID.randomUUID().toString();
    }

    private void sendActivationEmail(String email, String code) {
        String message = """
                Your account verification code is: %s
                                
                Please enter your code on following link: https://cook-for-me.onrender.com/register/confirm
                                
                Sincerely, Cook For Me team!
                """.formatted(code);

        mailService.sendMessage(email, "Cook For Me - Verification code", message);
    }

    private void checkCode(String code1, String code2) {
        if(!code1.equals(code2)) {
            throw new CookForMeException(ErrorCode.VERIFICATION_CODE_INCORRECT);
        }
    }

}
