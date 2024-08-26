package com.postgraduate.domain.payment.domain.service;

import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.repository.PaymentRepository;
import com.postgraduate.domain.payment.exception.PaymentNotFoundException;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.user.domain.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.postgraduate.domain.payment.domain.entity.constant.PaymentStatus.DONE;
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

    private Payment payment = mock(Payment.class);
    private User user = mock(User.class);
    private Senior senior = mock(Senior.class);
    private String orderId = "1";
    private Long paymentId = 1L;

    @Test
    @DisplayName("paymentId 조회 테스트")
    void byId() {
        given(paymentRepository.findById(paymentId))
                .willReturn(Optional.of(payment));

        assertThat(paymentGetService.byId(paymentId))
                .isEqualTo(payment);
    }

    @Test
    @DisplayName("paymentId 조회 실패 테스트")
    void byIdFail() {
        given(paymentRepository.findById(paymentId))
                .willReturn(Optional.ofNullable(null));

        assertThatThrownBy(() -> paymentGetService.byId(paymentId))
                .isInstanceOf(PaymentNotFoundException.class);
    }

    @Test
    @DisplayName("User기반 DONE 조회 테스트 ")
    void byUserAndOrderId() {
        given(paymentRepository.findByUserAndOrderIdAndStatus(user, orderId, DONE))
                .willReturn(Optional.of(payment));

        assertThat(paymentGetService.byUserAndOrderId(user, orderId))
                .isEqualTo(payment);
    }

    @Test
    @DisplayName("User기반 DONE 조회 실패 테스트")
    void byUserAndOrderIdFail() {
        given(paymentRepository.findByUserAndOrderIdAndStatus(user, orderId, DONE))
                .willReturn(Optional.ofNullable(null));

        assertThatThrownBy(() -> paymentGetService.byUserAndOrderId(user, orderId))
                .isInstanceOf(PaymentNotFoundException.class);
    }

    @Test
    @DisplayName("Senior기반 DONE 조회 테스트 ")
    void bySeniorAndOrderId() {
        given(paymentRepository.findBySeniorAndOrderIdAndStatus(senior, orderId, DONE))
                .willReturn(Optional.of(payment));

        assertThat(paymentGetService.bySeniorAndOrderId(senior, orderId))
                .isEqualTo(payment);
    }

    @Test
    @DisplayName("Senior기반 DONE 조회 실패 테스트")
    void bySeniorAndOrderIdFail() {
        given(paymentRepository.findBySeniorAndOrderIdAndStatus(senior, orderId, DONE))
                .willReturn(Optional.ofNullable(null));

        assertThatThrownBy(() -> paymentGetService.bySeniorAndOrderId(senior, orderId))
                .isInstanceOf(PaymentNotFoundException.class);
    }
}