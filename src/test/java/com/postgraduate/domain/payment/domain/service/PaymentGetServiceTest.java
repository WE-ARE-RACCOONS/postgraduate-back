package com.postgraduate.domain.payment.domain.service;

import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.repository.PaymentRepository;
import com.postgraduate.domain.payment.exception.PaymentNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class PaymentGetServiceTest {
    @Mock
    private PaymentRepository paymentRepository;
    @InjectMocks
    private PaymentGetService paymentGetService;

    @Test
    @DisplayName("payment 조회 안될 경우 예외 테스트")
    void byOrderIdFail() {
        given(paymentRepository.findByOrderId(any()))
                        .willReturn(ofNullable(null));

        assertThatThrownBy(() -> paymentGetService.byOrderId(any()))
                .isInstanceOf(PaymentNotFoundException.class);
    }

    @Test
    @DisplayName("payment 조회 테스트")
    void byOrderId() {
        Payment payment = mock(Payment.class);

        given(paymentRepository.findByOrderId(any()))
                .willReturn(of(payment));

        assertThat(paymentGetService.byOrderId(any()))
                .isEqualTo(payment);
    }
}