package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.mentoring.application.dto.res.AppliedMentoringResponse;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.mentoring.exception.MentoringDetailNotFoundException;
import com.postgraduate.domain.mentoring.exception.MentoringNotFoundException;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.entity.constant.Status;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.senior.domain.entity.Info;
import com.postgraduate.domain.senior.domain.entity.Profile;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.*;
import static com.postgraduate.domain.senior.domain.entity.constant.Status.APPROVE;
import static com.postgraduate.domain.user.domain.entity.constant.Role.SENIOR;
import static com.postgraduate.domain.user.domain.entity.constant.Role.USER;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class MentoringUserInfoUseCaseTest {
    @Mock
    private MentoringGetService mentoringGetService;

    @InjectMocks
    private MentoringUserInfoUseCase mentoringUserInfoUseCase;

    private Long mentoringId = -1L;
    private User user;
    private Senior senior;
    private Info info;
    private Profile profile;
    private Salary salary;
    private Payment payment;
    private User mentoringUser;
    private Mentoring mentoring;

    @BeforeEach
    void setting() {
        info = new Info("a", "a", "a", "a", "a", "a", TRUE, TRUE, "a");
        profile = new Profile("a", "a", "a", "a", 30);
        user = new User(-1L, 1234L, "a",
                "a", "123", "a",
                0, SENIOR, TRUE, LocalDateTime.now(), LocalDateTime.now(), TRUE);
        mentoringUser = new User(-2L, 12345L, "a",
                "a", "123", "a",
                0, USER, TRUE, LocalDateTime.now(), LocalDateTime.now(), TRUE);
        senior = new Senior(-1L, user, "a",
                APPROVE, 1, info, profile,
                LocalDateTime.now(), LocalDateTime.now());
        salary = new Salary(-1L, FALSE, senior, 10000, LocalDate.now(), LocalDateTime.now(), null);
        payment = new Payment(-1L, mentoringUser, senior, 20000, "a", "a", "a", LocalDateTime.now(), null, Status.DONE);
        mentoring = new Mentoring(-1L, mentoringUser, senior, payment, salary, "asd", "asd", "1201,1202,1203", 30, WAITING, LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    @DisplayName("Detail 반환 테스트")
    void getMentoringDetail() {
        given(mentoringGetService.byIdAndUserForDetails(mentoringId, user))
                .willReturn(mentoring);

        assertThat(mentoringUserInfoUseCase.getMentoringDetail(user, mentoringId))
                .isNotNull();
    }

    @Test
    @DisplayName("Detail 반환 실패 테스트")
    void getMentoringDetailFail() {
        given(mentoringGetService.byIdAndUserForDetails(mentoringId, user))
                .willThrow(MentoringNotFoundException.class);

        assertThatThrownBy(() -> mentoringUserInfoUseCase.getMentoringDetail(user, mentoringId))
                .isInstanceOf(MentoringNotFoundException.class);
    }

    @Test
    @DisplayName("WAITING 반환 테스트")
    void getWaiting() {
        Mentoring mentoring1 = new Mentoring(1L, user, senior, payment, null
                , "a", "b", "c"
                , 40, WAITING
                , LocalDateTime.now(), LocalDateTime.now());
        Mentoring mentoring2 = new Mentoring(2L, user, senior, payment, null
                , "a", "b", "c"
                , 40, WAITING
                , LocalDateTime.now(), LocalDateTime.now());
        Mentoring mentoring3 = new Mentoring(3L, user, senior, payment, null
                , "a", "b", "c"
                , 40, WAITING
                , LocalDateTime.now(), LocalDateTime.now());
        List<Mentoring> mentorings = List.of(mentoring1, mentoring2, mentoring3);

        given(mentoringGetService.byUserWaiting(user))
                .willReturn(mentorings);

        AppliedMentoringResponse waiting = mentoringUserInfoUseCase.getWaiting(user);

        assertThat(waiting.mentoringInfos())
                .hasSameSizeAs(mentorings);
    }

    @Test
    @DisplayName("EXPECTED 반환 테스트")
    void getExpected() {
        Mentoring mentoring1 = new Mentoring(1L, user, senior, payment, null
                , "a", "b", "c"
                , 40, EXPECTED
                , LocalDateTime.now(), LocalDateTime.now());
        Mentoring mentoring2 = new Mentoring(2L, user, senior, payment, null
                , "a", "b", "c"
                , 40, EXPECTED
                , LocalDateTime.now(), LocalDateTime.now());
        Mentoring mentoring3 = new Mentoring(3L, user, senior, payment, null
                , "a", "b", "c"
                , 40, EXPECTED
                , LocalDateTime.now(), LocalDateTime.now());
        List<Mentoring> mentorings = List.of(mentoring1, mentoring2, mentoring3);

        given(mentoringGetService.byUserExpected(user))
                .willReturn(mentorings);

        AppliedMentoringResponse expected = mentoringUserInfoUseCase.getExpected(user);

        assertThat(expected.mentoringInfos())
                .hasSameSizeAs(mentorings);
    }

    @Test
    @DisplayName("DONE 반환 테스트")
    void getDone() {
        Mentoring mentoring1 = new Mentoring(1L, user, senior, payment, null
                , "a", "b", "c"
                , 40, DONE
                , LocalDateTime.now(), LocalDateTime.now());
        Mentoring mentoring2 = new Mentoring(2L, user, senior, payment, null
                , "a", "b", "c"
                , 40, DONE
                , LocalDateTime.now(), LocalDateTime.now());
        Mentoring mentoring3 = new Mentoring(3L, user, senior, payment, null
                , "a", "b", "c"
                , 40, DONE
                , LocalDateTime.now(), LocalDateTime.now());
        List<Mentoring> mentorings = List.of(mentoring1, mentoring2, mentoring3);

        given(mentoringGetService.byUserDone(user))
                .willReturn(mentorings);

        AppliedMentoringResponse done = mentoringUserInfoUseCase.getDone(user);

        assertThat(done.mentoringInfos())
                .hasSameSizeAs(mentorings);
    }
}