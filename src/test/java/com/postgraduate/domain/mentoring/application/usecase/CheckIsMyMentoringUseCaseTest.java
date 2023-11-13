package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.user.domain.entity.Hope;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.entity.constant.Role;
import com.postgraduate.global.exception.ApplicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckIsMyMentoringUseCaseTest {
    @Mock
    private MentoringGetService mentoringGetService;

    @InjectMocks
    private CheckIsMyMentoringUseCase checkIsMyMentoringUseCase;

    User user;
    @BeforeEach
    void setUser() {
        Hope hope = new Hope("computer","ai", true);
        user = new User(100000000L, 12345L, "test.com",
                "test", "test.png", "01012341234", 0, Role.USER, hope, false,
                LocalDate.now(), LocalDate.now());
    }

    @Test
    public void testCheckByRoleInvalid() {
        Long mentoringId = 1L;
        Mentoring mentoring = mock();

        when(mentoring.getUser()).thenReturn(new User());
        when(mentoringGetService.byMentoringId(mentoringId)).thenReturn(mentoring);

        assertThrows(ApplicationException.class, () -> {
            checkIsMyMentoringUseCase.byUser(user, mentoringId);
        });
    }
}