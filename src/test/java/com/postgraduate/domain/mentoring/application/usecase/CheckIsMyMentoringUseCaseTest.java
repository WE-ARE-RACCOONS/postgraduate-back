package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.user.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CheckIsMyMentoringUseCaseTest {
    @Mock
    private MentoringGetService mentoringGetService;
    @Mock
    private SeniorGetService seniorGetService;

    private CheckIsMyMentoringUseCase checkIsMyMentoringUseCase;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        checkIsMyMentoringUseCase = new CheckIsMyMentoringUseCase(
                mentoringGetService,
                seniorGetService
        );
    }

    @Test
    public void testCheckByRoleInvalid() {
        User user = new User();
        Long mentoringId = 1L;
        Mentoring mentoring = mock();
        Senior senior = mock();

        when(seniorGetService.byUser(user)).thenReturn(senior);
        when(mentoring.getUser()).thenReturn(new User());
        when(mentoringGetService.byMentoringId(mentoringId)).thenReturn(mentoring);

        assertThrows(IllegalArgumentException.class, () -> {
            checkIsMyMentoringUseCase.checkByRole(user, mentoringId);
        });
    }
}