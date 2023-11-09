package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.global.auth.AuthDetails;
import com.postgraduate.global.config.security.util.SecurityUtils;
import com.postgraduate.global.exception.ApplicationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckIsMyMentoringUseCaseTest {
    @Mock
    private MentoringGetService mentoringGetService;

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private CheckIsMyMentoringUseCase checkIsMyMentoringUseCase;

    @Test
    public void testCheckByRoleInvalid() {
        AuthDetails authDetails = mock(AuthDetails.class);
        when(securityUtils.getLoggedInUser(authDetails)).thenReturn(new User());
        Long mentoringId = 1L;
        Mentoring mentoring = mock();

        when(mentoring.getUser()).thenReturn(new User());
        when(mentoringGetService.byMentoringId(mentoringId)).thenReturn(mentoring);

        assertThrows(ApplicationException.class, () -> {
            checkIsMyMentoringUseCase.byUser(authDetails, mentoringId);
        });
    }
}