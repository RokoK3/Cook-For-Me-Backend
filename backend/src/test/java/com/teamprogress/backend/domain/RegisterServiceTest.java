package com.teamprogress.backend.domain;

import com.teamprogress.backend.persistance.entity.User;
import com.teamprogress.backend.persistance.entity.model.AccountStatus;
import com.teamprogress.backend.rest.model.requests.ConfirmRegisterRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class RegisterServiceTest {

    @InjectMocks
    private RegisterService registerService;

    @Mock
    private UserService userService;
    @Mock
    private CookService cookService;
    @Mock
    private MailService mailService;
    @Spy
    private PasswordEncoder passwordEncoder;
    @Mock
    private User givenUser;

    @Test
    void shouldActivateAccount() {
        // given
        var givenUsername = "TestUser";
        var givenPassword = "TestPassword!123";
        var givenActivationCode = "activationCodeSampleMessageTest!123.";
        var givenConfirmRegisterRequest = givenConfirmRegisterRequest(givenUsername, givenPassword, givenActivationCode);

        given(cookService.existsByUsername(givenUsername)).willReturn(false);
        given(userService.existsByUsername(givenUsername)).willReturn(true);
        given(userService.existsByUsernameAndActivated(givenUsername)).willReturn(false);
        given(userService.getByUsername(givenUsername)).willReturn(givenUser);
        given(userService.save(givenUser)).willReturn(givenUser);
        given(givenUser.getVerificationCode()).willReturn(givenActivationCode);
        given(givenUser.getPassword()).willReturn(givenPassword);

        // when
        registerService.activateAccount(givenConfirmRegisterRequest);

        // then
        then(cookService).should().existsByUsername(givenUsername);
        then(cookService).shouldHaveNoMoreInteractions();
        then(givenUser).should().setPassword(any());
        then(givenUser).should().setStatus(AccountStatus.ACTIVATED);
        then(userService).should().save(givenUser);
    }

    private ConfirmRegisterRequest givenConfirmRegisterRequest(String givenUsername, String givenPassword, String givenActivationCode) {
        return new ConfirmRegisterRequest(givenUsername, givenPassword, givenActivationCode);
    }

}
