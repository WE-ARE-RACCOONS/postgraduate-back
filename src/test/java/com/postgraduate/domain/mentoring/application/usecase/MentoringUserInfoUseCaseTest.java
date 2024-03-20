package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.mentoring.application.dto.res.AppliedMentoringResponse;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.mentoring.exception.MentoringDetailNotFoundException;
import com.postgraduate.domain.payment.domain.entity.Payment;
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

import java.time.LocalDateTime;
import java.util.List;

import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.*;
import static com.postgraduate.domain.senior.domain.entity.constant.Status.APPROVE;
import static com.postgraduate.domain.user.domain.entity.constant.Role.USER;
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

    private Long mentoringId = 1L;
    private User user;
    private Senior senior;
    private Info info;
    private Profile profile;
    private Mentoring mentoring;

    @BeforeEach
    void setting() {
        info = new Info("a", "a", "a", "a", "a", "a", TRUE, TRUE, "a");
        profile = new Profile("a", "a", "a", "a", 40);
        user = new User(1L, 1234L, "a",
                "a", "123", "a",
                1, USER, TRUE, LocalDateTime.now(), LocalDateTime.now(), TRUE);
        senior = new Senior(1L, user, "a",
                APPROVE, 1, info, profile,
                LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    @DisplayName("Detail 반환 테스트")
    void getMentoringDetail() {
        Payment payment = mock(Payment.class);

        mentoring = new Mentoring(mentoringId, user, senior, payment, null
                , "a", "b", "c"
                , 40, EXPECTED
                , LocalDateTime.now(), LocalDateTime.now());

        given(mentoringGetService.byMentoringId(any()))
                .willReturn(mentoring);

        assertThat(mentoringUserInfoUseCase.getMentoringDetail(user, mentoringId))
                .isNotNull();
    }

    @Test
    @DisplayName("Detail 반환 실패 테스트 - DONE")
    void getMentoringDetailFailWithDone() {
        Payment payment = mock(Payment.class);

        mentoring = new Mentoring(mentoringId, user, senior, payment, null
                , "a", "b", "c"
                , 40, DONE
                , LocalDateTime.now(), LocalDateTime.now());

        given(mentoringGetService.byMentoringId(any()))
                .willReturn(mentoring);

        assertThatThrownBy(() -> mentoringUserInfoUseCase.getMentoringDetail(user, mentoringId))
                .isInstanceOf(MentoringDetailNotFoundException.class);
    }

    @Test
    @DisplayName("Detail 반환 실패 테스트 - REFUSE")
    void getMentoringDetailFailWithRefuse() {
        Payment payment = mock(Payment.class);

        mentoring = new Mentoring(mentoringId, user, senior, payment, null
                , "a", "b", "c"
                , 40, REFUSE
                , LocalDateTime.now(), LocalDateTime.now());

        given(mentoringGetService.byMentoringId(any()))
                .willReturn(mentoring);

        assertThatThrownBy(() -> mentoringUserInfoUseCase.getMentoringDetail(user, mentoringId))
                .isInstanceOf(MentoringDetailNotFoundException.class);
    }

    @Test
    @DisplayName("Detail 반환 실패 테스트 - CANCEL")
    void getMentoringDetailFailWithCancel() {
        Payment payment = mock(Payment.class);

        mentoring = new Mentoring(mentoringId, user, senior, payment, null
                , "a", "b", "c"
                , 40, CANCEL
                , LocalDateTime.now(), LocalDateTime.now());

        given(mentoringGetService.byMentoringId(any()))
                .willReturn(mentoring);

        assertThatThrownBy(() -> mentoringUserInfoUseCase.getMentoringDetail(user, mentoringId))
                .isInstanceOf(MentoringDetailNotFoundException.class);
    }

    @Test
    @DisplayName("WAITING 반환 테스트")
    void getWaiting() {
        Payment payment = mock(Payment.class);

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

        given(mentoringGetService.byUser(user, WAITING))
                .willReturn(mentorings);

        AppliedMentoringResponse waiting = mentoringUserInfoUseCase.getWaiting(user);

        assertThat(waiting.mentoringInfos())
                .hasSameSizeAs(mentorings);
    }

    @Test
    @DisplayName("EXPECTED 반환 테스트")
    void getExpected() {
        Payment payment = mock(Payment.class);

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

        given(mentoringGetService.byUser(user, EXPECTED))
                .willReturn(mentorings);

        AppliedMentoringResponse expected = mentoringUserInfoUseCase.getExpected(user);

        assertThat(expected.mentoringInfos())
                .hasSameSizeAs(mentorings);
    }

    @Test
    @DisplayName("DONE 반환 테스트")
    void getDone() {
        Payment payment = mock(Payment.class);

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

        given(mentoringGetService.byUser(user, DONE))
                .willReturn(mentorings);

        AppliedMentoringResponse done = mentoringUserInfoUseCase.getDone(user);

        assertThat(done.mentoringInfos())
                .hasSameSizeAs(mentorings);
    }
}