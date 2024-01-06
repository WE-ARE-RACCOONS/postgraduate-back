package com.postgraduate.domain.payment.domain.service;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.repository.PaymentRepository;
import com.postgraduate.domain.payment.exception.PaymentNotFoundException;
import com.postgraduate.domain.senior.domain.entity.Senior;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class PaymentGetServiceTest {
    @Mock
    private PaymentRepository paymentRepository;
    @InjectMocks
    private PaymentGetService paymentGetService;

    @Test
    @DisplayName("mentoring 결제 조회 안될 경우 예외 테스트")
    void byMentoringFail() {
        Mentoring mentoring = mock(Mentoring.class);
        given(paymentRepository.findByMentoring(mentoring))
                        .willReturn(ofNullable(null));

        assertThatThrownBy(() -> paymentGetService.byMentoring(mentoring))
                .isInstanceOf(PaymentNotFoundException.class);
    }

    @Test
    @DisplayName("mentoring 결제 조회 테스트")
    void byMentoring() {
        Mentoring mentoring = mock(Mentoring.class);
        Payment payment = mock(Payment.class);

        given(paymentRepository.findByMentoring(mentoring))
                .willReturn(of(payment));

        assertThat(paymentGetService.byMentoring(mentoring))
                .isEqualTo(payment);
    }

    @Test
    @DisplayName("senior와 TRUE 조회 테스트")
    void bySeniorAndTrue() {
        Senior senior = mock(Senior.class);
        Payment payment1 = mock(Payment.class);
        Payment payment2 = mock(Payment.class);
        Payment payment3 = mock(Payment.class);
        List<Payment> payments = List.of(payment1, payment2, payment3);

        given(paymentRepository.findAllBySeniorAndStatus(senior, TRUE))
                .willReturn(payments);

        assertThat(paymentGetService.bySeniorAndStatus(senior, TRUE))
                .isEqualTo(payments);
    }

    @Test
    @DisplayName("senior와 FALSE 조회 테스트")
    void bySeniorAndFalse() {
        Senior senior = mock(Senior.class);
        Payment payment1 = mock(Payment.class);
        Payment payment2 = mock(Payment.class);
        Payment payment3 = mock(Payment.class);
        List<Payment> payments = List.of(payment1, payment2, payment3);

        given(paymentRepository.findAllBySeniorAndStatus(senior, FALSE))
                .willReturn(payments);

        assertThat(paymentGetService.bySeniorAndStatus(senior, FALSE))
                .isEqualTo(payments);
    }
}