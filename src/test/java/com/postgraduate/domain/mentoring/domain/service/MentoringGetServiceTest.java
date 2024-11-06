package com.postgraduate.domain.mentoring.domain.service;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.repository.MentoringRepository;
import com.postgraduate.domain.mentoring.exception.MentoringNotFoundException;
import com.postgraduate.domain.mentoring.exception.MentoringPresentException;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.member.senior.domain.entity.Senior;
import com.postgraduate.domain.member.user.domain.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.postgraduate.domain.mentoring.domain.entity.constant.MentoringStatus.*;
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
}