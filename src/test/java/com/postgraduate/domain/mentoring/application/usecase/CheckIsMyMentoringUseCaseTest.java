package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.auth.exception.PermissionDeniedException;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.user.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.postgraduate.domain.user.domain.entity.constant.Role.SENIOR;
import static com.postgraduate.domain.user.domain.entity.constant.Role.USER;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class CheckIsMyMentoringUseCaseTest {
    @Mock
    private MentoringGetService mentoringGetService;

    @InjectMocks
    private CheckIsMyMentoringUseCase checkIsMyMentoringUseCase;

    private User user1;
    private User user2;
    private User senior1;
    private User senior2;
    private Long mentoringId = 1L;

    @BeforeEach
    void setUser() {
        user1 = new User(-1L, -1234L, "abc.com", "abc"
                , " 123123", "abcab", 0
                , USER, TRUE, LocalDateTime.now(), LocalDateTime.now(), FALSE);
        user2 = new User(-11L, -12345L, "abc.com", "qwe"
                , " 123123", "abcab", 0
                , USER, TRUE, LocalDateTime.now(), LocalDateTime.now(), FALSE);

        senior1 = new User(-2L, -2345L, "abc.com", "zxc"
                , " 123123", "abcab", 0
                , SENIOR, TRUE, LocalDateTime.now(), LocalDateTime.now(), FALSE);
        senior2 = new User(-22L, -23456L, "abc.com", "asdf"
                , " 123123", "abcab", 0
                , SENIOR, TRUE, LocalDateTime.now(), LocalDateTime.now(), FALSE);
    }

    @Test
    @DisplayName("다른 유저의 멘토링의 경우 - USER")
    public void testCheckUserNotOk() {
        Mentoring mentoring = mock(Mentoring.class);
        given(mentoring.getUser()).willReturn(user2);
        given(mentoringGetService.byMentoringId(mentoringId)).willReturn(mentoring);

        assertThatThrownBy(() -> checkIsMyMentoringUseCase.byUser(user1, mentoringId))
                .isInstanceOf(PermissionDeniedException.class);
        assertThatThrownBy(() -> checkIsMyMentoringUseCase.byUser(senior1, mentoringId))
                .isInstanceOf(PermissionDeniedException.class);
        assertThatThrownBy(() -> checkIsMyMentoringUseCase.byUser(senior2, mentoringId))
                .isInstanceOf(PermissionDeniedException.class);
    }

    @Test
    @DisplayName("자신의 멘토링 체크 - USER")
    public void checkUserIsOk() {
        Mentoring mentoring = mock(Mentoring.class);
        given(mentoring.getUser()).willReturn(user1);
        given(mentoringGetService.byMentoringId(mentoringId)).willReturn(mentoring);

        assertThat(checkIsMyMentoringUseCase.byUser(user1, mentoringId))
                .isEqualTo(mentoring);
    }

    @Test
    @DisplayName("다른 유저의 멘토링의 경우 - SENIOR")
    public void checkSeniorNotOk() {
        Mentoring mentoring = mock(Mentoring.class);
        given(mentoring.getUser()).willReturn(senior1);
        given(mentoringGetService.byMentoringId(mentoringId)).willReturn(mentoring);

        assertThatThrownBy(() -> checkIsMyMentoringUseCase.byUser(senior2, mentoringId))
                .isInstanceOf(PermissionDeniedException.class);
        assertThatThrownBy(() -> checkIsMyMentoringUseCase.byUser(user1, mentoringId))
                .isInstanceOf(PermissionDeniedException.class);
        assertThatThrownBy(() -> checkIsMyMentoringUseCase.byUser(user2, mentoringId))
                .isInstanceOf(PermissionDeniedException.class);
    }

    @Test
    @DisplayName("자신의 멘토링 체크 - SENIOR")
    public void checkSeniorIsOk() {
        Mentoring mentoring = mock(Mentoring.class);
        given(mentoring.getUser()).willReturn(senior1);
        given(mentoringGetService.byMentoringId(mentoringId)).willReturn(mentoring);

        assertThat(checkIsMyMentoringUseCase.byUser(senior1, mentoringId))
                .isEqualTo(mentoring);
    }
}