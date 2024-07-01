package com.postgraduate.domain.mentoring.domain.service;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.domain.mentoring.domain.repository.MentoringRepository;
import com.postgraduate.domain.mentoring.exception.MentoringNotFoundException;
import com.postgraduate.domain.mentoring.exception.MentoringPresentException;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.*;
import static java.lang.Boolean.FALSE;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MentoringGetServiceTest {
    @Mock
    private MentoringRepository mentoringRepository;
    @InjectMocks
    private MentoringGetService mentoringGetService;

    private Long mentoringId = -1L;
    private Mentoring mentoring = mock(Mentoring.class);
    private User user = mock(User.class);
    private Senior senior = mock(Senior.class);
    private Payment payment = mock(Payment.class);

    @Test
    @DisplayName("이미 존재하는 결제건 예외 테스트")
    void checkByPaymentFail() {
        given(mentoringRepository.findByPayment(payment))
                .willReturn(Optional.of(mentoring));

        assertThatThrownBy(() -> mentoringGetService.checkByPayment(payment))
                .isInstanceOf(MentoringPresentException.class);
    }

    @Test
    @DisplayName("payment로 조회시 null 가능 테스트")
    void byPaymentWithNullIsOk() {
        given(mentoringRepository.findByPayment(payment))
                .willReturn(Optional.ofNullable(null));

        assertThat(mentoringGetService.byPaymentWithNull(payment))
                .isNull();

        given(mentoringRepository.findByPayment(payment))
                .willReturn(Optional.of(mentoring));

        assertThat(mentoringGetService.byPaymentWithNull(payment))
                .isEqualTo(mentoring);
    }

    @Test
    @DisplayName("payment로 조회시 null 불가능 테스트")
    void byPayment() {
        given(mentoringRepository.findByPayment(payment))
                .willReturn(Optional.ofNullable(null));

        assertThatThrownBy(() -> mentoringGetService.byPayment(payment))
                .isInstanceOf(MentoringNotFoundException.class);

        given(mentoringRepository.findByPayment(payment))
                .willReturn(Optional.of(mentoring));

        assertThat(mentoringGetService.byPayment(payment))
                .isEqualTo(mentoring);
    }

    @Test
    @DisplayName("User조회 및 각각 상태별 조회 테스트")
    void byUserWithStatus() {
        given(mentoringRepository.findAllByUserAndStatus(user, WAITING))
                .willReturn(List.of(mentoring));
        given(mentoringRepository.findAllByUserAndStatus(user, EXPECTED))
                .willReturn(List.of(mentoring));
        given(mentoringRepository.findAllByUserAndStatus(user, DONE))
                .willReturn(List.of(mentoring));

        assertThat(mentoringGetService.byUserWaiting(user))
                .isEqualTo(List.of(mentoring));
        assertThat(mentoringGetService.byUserExpected(user))
                .isEqualTo(List.of(mentoring));
        assertThat(mentoringGetService.byUserDone(user))
                .isEqualTo(List.of(mentoring));
    }

    @Test
    @DisplayName("Senior조회 및 각각 상태별 조회 테스트")
    void bySeniorWithStatus() {
        given(mentoringRepository.findAllBySeniorAndStatus(senior, WAITING))
                .willReturn(List.of(mentoring));
        given(mentoringRepository.findAllBySeniorAndStatus(senior, EXPECTED))
                .willReturn(List.of(mentoring));
        given(mentoringRepository.findAllBySeniorAndStatus(senior, DONE))
                .willReturn(List.of(mentoring));

        assertThat(mentoringGetService.bySeniorWaiting(senior))
                .isEqualTo(List.of(mentoring));
        assertThat(mentoringGetService.bySeniorExpected(senior))
                .isEqualTo(List.of(mentoring));
        assertThat(mentoringGetService.bySeniorDone(senior))
                .isEqualTo(List.of(mentoring));
    }
    @Test
    @DisplayName("mentoringId를 통해 조회 테스트")
    void byMentoringId() {
        given(mentoringRepository.findByMentoringId(mentoringId))
                .willReturn(ofNullable(null));

        assertThatThrownBy(() -> mentoringGetService.byMentoringId(mentoringId))
                .isInstanceOf(MentoringNotFoundException.class);

        given(mentoringRepository.findByMentoringId(mentoringId))
                .willReturn(of(mentoring));

        assertThat(mentoringGetService.byMentoringId(mentoringId))
                .isEqualTo(mentoring);
    }

    @Test
    @DisplayName("User 멘토링 상세 조회 테스트")
    void byIdAndUserForDetails() {
        given(mentoringRepository.findByMentoringIdAndUserForDetails(mentoringId, user))
                .willReturn(Optional.of(mentoring));

        assertThat(mentoringGetService.byIdAndUserForDetails(mentoringId, user))
                .isEqualTo(mentoring);

        given(mentoringRepository.findByMentoringIdAndUserForDetails(mentoringId, user))
                .willReturn(Optional.ofNullable(null));

        assertThatThrownBy(() -> mentoringGetService.byIdAndUserForDetails(mentoringId, user))
                .isInstanceOf(MentoringNotFoundException.class);
    }

    @Test
    @DisplayName("Senior 멘토링 상세 조회 테스트")
    void byIdAndSeniorForDetails() {
        given(mentoringRepository.findByMentoringIdAndSeniorForDetails(mentoringId, senior))
                .willReturn(Optional.of(mentoring));

        assertThat(mentoringGetService.byIdAndSeniorForDetails(mentoringId, senior))
                .isEqualTo(mentoring);

        given(mentoringRepository.findByMentoringIdAndSeniorForDetails(mentoringId, senior))
                .willReturn(Optional.ofNullable(null));

        assertThatThrownBy(() -> mentoringGetService.byIdAndSeniorForDetails(mentoringId, senior))
                .isInstanceOf(MentoringNotFoundException.class);
    }

    @Test
    @DisplayName("User 기반 멘토링 상태별 조회 테스트")
    void byIdAndUserAndStatusTest() {
        given(mentoringRepository.findByMentoringIdAndUserAndStatus(mentoringId, user, WAITING))
                .willReturn(Optional.of(mentoring));
        assertThat(mentoringGetService.byIdAndUserAndWaiting(mentoringId, user))
                .isEqualTo(mentoring);

        given(mentoringRepository.findByMentoringIdAndUserAndStatus(mentoringId, user, EXPECTED))
                .willReturn(Optional.of(mentoring));
        assertThat(mentoringGetService.byIdAndUserAndExpected(mentoringId, user))
                .isEqualTo(mentoring);

        given(mentoringRepository.findByMentoringIdAndUserAndStatus(mentoringId, user, WAITING))
                .willReturn(Optional.ofNullable(null));
        assertThatThrownBy(() ->mentoringGetService.byIdAndUserAndWaiting(mentoringId, user))
                .isInstanceOf(MentoringNotFoundException.class);

        given(mentoringRepository.findByMentoringIdAndUserAndStatus(mentoringId, user, EXPECTED))
                .willReturn(Optional.ofNullable(null));
        assertThatThrownBy(() ->mentoringGetService.byIdAndUserAndExpected(mentoringId, user))
                .isInstanceOf(MentoringNotFoundException.class);

        given(mentoringRepository.findByMentoringIdAndUserAndStatus(mentoringId, user, DONE))
                .willReturn(Optional.ofNullable(null));
    }

    @Test
    @DisplayName("Senior 기반 멘토링 상태별 조회 테스트")
    void byIdAndSeniorAndStatusTest() {
        given(mentoringRepository.findByMentoringIdAndSeniorAndStatus(mentoringId, senior, WAITING))
                .willReturn(Optional.of(mentoring));
        assertThat(mentoringGetService.byIdAndSeniorAndWaiting(mentoringId, senior))
                .isEqualTo(mentoring);

        given(mentoringRepository.findByMentoringIdAndSeniorAndStatus(mentoringId, senior, EXPECTED))
                .willReturn(Optional.of(mentoring));
        assertThat(mentoringGetService.byIdAndSeniorAndExpected(mentoringId, senior))
                .isEqualTo(mentoring);

        given(mentoringRepository.findByMentoringIdAndSeniorAndStatus(mentoringId, senior, DONE))
                .willReturn(Optional.of(mentoring));

        given(mentoringRepository.findByMentoringIdAndSeniorAndStatus(mentoringId, senior, WAITING))
                .willReturn(Optional.ofNullable(null));
        assertThatThrownBy(() ->mentoringGetService.byIdAndSeniorAndWaiting(mentoringId, senior))
                .isInstanceOf(MentoringNotFoundException.class);

        given(mentoringRepository.findByMentoringIdAndSeniorAndStatus(mentoringId, senior, EXPECTED))
                .willReturn(Optional.ofNullable(null));
        assertThatThrownBy(() ->mentoringGetService.byIdAndSeniorAndExpected(mentoringId, senior))
                .isInstanceOf(MentoringNotFoundException.class);
    }
}