package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.mentoring.application.dto.res.SeniorMentoringResponse;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.mentoring.exception.MentoringDetailNotFoundException;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.entity.constant.Status;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.user.domain.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;

@ExtendWith(MockitoExtension.class)
class MentoringSeniorInfoUseCaseTest {
    @Mock
    private MentoringGetService mentoringGetService;

    @Mock
    private CheckIsMyMentoringUseCase checkIsMyMentoringUseCase;

    @Mock
    private SeniorGetService seniorGetService;

    @Mock
    private SalaryGetService salaryGetService;

    @InjectMocks
    private MentoringSeniorInfoUseCase mentoringSeniorInfoUseCase;

    @Test
    @DisplayName("Detail 반환 테스트")
    void getSeniorMentoringDetail() {
        Long mentoringId = 1L;
        User user = mock(User.class);
        Senior senior = mock(Senior.class);
        Payment payment = mock(Payment.class);

        Mentoring mentoring = new Mentoring(mentoringId, user, senior, payment
                , "a", "b", "c"
                , 40, EXPECTED
                , LocalDateTime.now(), LocalDateTime.now());

        given(seniorGetService.byUser(user))
                .willReturn(senior);
        given(checkIsMyMentoringUseCase.bySenior(senior, mentoringId))
                .willReturn(mentoring);

        assertThat(mentoringSeniorInfoUseCase.getSeniorMentoringDetail(user, mentoringId))
                .isNotNull();
    }

    @Test
    @DisplayName("Detail 반환 실패 테스트 - DONE")
    void getSeniorMentoringDetailFail() {
        Long mentoringId = 1L;
        User user = mock(User.class);
        Senior senior = mock(Senior.class);
        Payment payment = mock(Payment.class);

        Mentoring mentoring = new Mentoring(mentoringId, user, senior, payment
                , "a", "b", "c"
                , 40, DONE
                , LocalDateTime.now(), LocalDateTime.now());

        given(seniorGetService.byUser(user))
                .willReturn(senior);
        given(checkIsMyMentoringUseCase.bySenior(senior, mentoringId))
                .willReturn(mentoring);

        assertThatThrownBy(() -> mentoringSeniorInfoUseCase.getSeniorMentoringDetail(user, mentoringId))
                .isInstanceOf(MentoringDetailNotFoundException.class);
    }

    @Test
    @DisplayName("Detail 반환 실패 테스트 - REFUSE")
    void getSeniorMentoringDetailFailWithRefuse() {
        Long mentoringId = 1L;
        User user = mock(User.class);
        Senior senior = mock(Senior.class);
        Payment payment = mock(Payment.class);

        Mentoring mentoring = new Mentoring(mentoringId, user, senior, payment
                , "a", "b", "c"
                , 40, REFUSE
                , LocalDateTime.now(), LocalDateTime.now());

        given(seniorGetService.byUser(user))
                .willReturn(senior);
        given(checkIsMyMentoringUseCase.bySenior(senior, mentoringId))
                .willReturn(mentoring);

        assertThatThrownBy(() -> mentoringSeniorInfoUseCase.getSeniorMentoringDetail(user, mentoringId))
                .isInstanceOf(MentoringDetailNotFoundException.class);
    }

    @Test
    @DisplayName("Detail 반환 실패 테스트 - CANCEL")
    void getSeniorMentoringDetailFailWithCancel() {
        Long mentoringId = 1L;
        User user = mock(User.class);
        Senior senior = mock(Senior.class);
        Payment payment = mock(Payment.class);

        Mentoring mentoring = new Mentoring(mentoringId, user, senior, payment
                , "a", "b", "c"
                , 40, CANCEL
                , LocalDateTime.now(), LocalDateTime.now());

        given(seniorGetService.byUser(user))
                .willReturn(senior);
        given(checkIsMyMentoringUseCase.bySenior(senior, mentoringId))
                .willReturn(mentoring);

        assertThatThrownBy(() -> mentoringSeniorInfoUseCase.getSeniorMentoringDetail(user, mentoringId))
                .isInstanceOf(MentoringDetailNotFoundException.class);
    }

    @Test
    @DisplayName("WAITING 반환 테스트")
    void getSeniorWaiting() {
        User user = mock(User.class);
        Senior senior = mock(Senior.class);
        Payment payment = mock(Payment.class);

        Mentoring mentoring1 = new Mentoring(1L, user, senior, payment, "A", "b", "a", 40, WAITING, LocalDateTime.now(), LocalDateTime.now());
        Mentoring mentoring2 = new Mentoring(2L, user, senior, payment, "A", "b", "a", 40, WAITING, LocalDateTime.now(), LocalDateTime.now());
        Mentoring mentoring3 = new Mentoring(3L, user, senior, payment, "A", "b", "a", 40, WAITING, LocalDateTime.now(), LocalDateTime.now());
        List<Mentoring> mentorings = List.of(mentoring1, mentoring2, mentoring3);

        given(seniorGetService.byUser(user))
                .willReturn(senior);
        given(mentoringGetService.bySenior(senior, WAITING))
                .willReturn(mentorings);

        SeniorMentoringResponse seniorWaiting = mentoringSeniorInfoUseCase.getSeniorWaiting(user);

        assertThat(seniorWaiting.seniorMentoringInfos().size())
                .isEqualTo(mentorings.size());
    }

    @Test
    @DisplayName("EXPECTED 반환 테스트")
    void getSeniorExpected() {
        User user = mock(User.class);
        Senior senior = mock(Senior.class);
        Payment payment = mock(Payment.class);

        Mentoring mentoring1 = new Mentoring(1L, user, senior, payment, "A", "b", "2024-01-20-17-00", 40, WAITING, LocalDateTime.now(), LocalDateTime.now());
        Mentoring mentoring2 = new Mentoring(2L, user, senior, payment, "A", "b", "2024-01-20-17-00", 40, WAITING, LocalDateTime.now(), LocalDateTime.now());
        Mentoring mentoring3 = new Mentoring(3L, user, senior, payment, "A", "b", "2024-01-20-17-00", 40, WAITING, LocalDateTime.now(), LocalDateTime.now());
        List<Mentoring> mentorings = List.of(mentoring1, mentoring2, mentoring3);

        given(seniorGetService.byUser(user))
                .willReturn(senior);
        given(mentoringGetService.bySenior(senior, EXPECTED))
                .willReturn(mentorings);

        SeniorMentoringResponse seniorExpected = mentoringSeniorInfoUseCase.getSeniorExpected(user);

        assertThat(seniorExpected.seniorMentoringInfos().size())
                .isEqualTo(mentorings.size());
    }

    @Test
    @DisplayName("DONE 반환 테스트")
    void getSeniorDone() {
        User user = mock(User.class);
        Senior senior = mock(Senior.class);
        Salary salary = mock(Salary.class);

        Payment payment1 = new Payment(1l, salary, user, 10000, "1", "1", "a", LocalDateTime.now(), LocalDateTime.now(), Status.DONE);
        Payment payment2 = new Payment(2l, salary, user, 10000, "1", "1", "a", LocalDateTime.now(), LocalDateTime.now(), Status.DONE);
        Payment payment3 = new Payment(3l, salary, user, 10000, "1", "1", "a", LocalDateTime.now(), LocalDateTime.now(), Status.DONE);

        Mentoring mentoring1 = new Mentoring(1L, user, senior, payment1, "A", "b", "a", 40, WAITING, LocalDateTime.now(), LocalDateTime.now());
        Mentoring mentoring2 = new Mentoring(2L, user, senior, payment2, "A", "b", "a", 40, WAITING, LocalDateTime.now(), LocalDateTime.now());
        Mentoring mentoring3 = new Mentoring(3L, user, senior, payment3, "A", "b", "a", 40, WAITING, LocalDateTime.now(), LocalDateTime.now());
        List<Mentoring> mentorings = List.of(mentoring1, mentoring2, mentoring3);

        given(seniorGetService.byUser(user))
                .willReturn(senior);
        given(mentoringGetService.bySenior(senior))
                .willReturn(mentorings);

        SeniorMentoringResponse seniorDone = mentoringSeniorInfoUseCase.getSeniorDone(user);

        assertThat(seniorDone.seniorMentoringInfos().size())
                .isEqualTo(mentorings.size());
    }
}
