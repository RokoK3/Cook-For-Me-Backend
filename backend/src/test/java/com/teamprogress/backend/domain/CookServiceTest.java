package com.teamprogress.backend.domain;

import com.teamprogress.backend.domain.model.CookForMeException;
import com.teamprogress.backend.persistance.repository.CookRepository;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CookServiceTest {

    @InjectMocks
    private CookService cookService;

    @Mock
    private CookRepository cookRepository;

    @Test
    void shouldThrow_whenCookNotFound() {
        // given
        var givenCookId = 12L;

        given(cookRepository.findById(givenCookId)).willReturn(Optional.empty());

        // when
        var thrown = catchThrowable(() -> cookService.getById(givenCookId));

        // then
        BDDMockito.then(cookRepository).should().findById(givenCookId);
        BDDAssertions.then(thrown).isInstanceOf(CookForMeException.class);
    }

}