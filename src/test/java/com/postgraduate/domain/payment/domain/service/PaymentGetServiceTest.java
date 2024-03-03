//package com.postgraduate.domain.payment.domain.service;
//
//import com.postgraduate.domain.payment.domain.entity.Payment;
//import com.postgraduate.domain.payment.domain.entity.constant.Status;
//import com.postgraduate.domain.payment.domain.repository.PaymentRepository;
//import com.postgraduate.domain.payment.exception.PaymentNotFoundException;
//import com.postgraduate.domain.user.domain.entity.User;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import static com.postgraduate.domain.payment.domain.entity.constant.Status.DONE;
//import static java.util.Optional.of;
//import static java.util.Optional.ofNullable;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.doReturn;
//import static org.mockito.Mockito.mock;
//
//@ExtendWith(MockitoExtension.class)
//class PaymentGetServiceTest {
//    @Mock
//    private PaymentRepository paymentRepository;
//    @InjectMocks
//    private PaymentGetService paymentGetService;
//
//    @Test
//    @DisplayName("payment 조회 안될 경우 예외 테스트")
//    void byOrderIdFail() {
//        given(paymentRepository.findByUserAndOrderIdAndStatus(any(), any(), any()))
//                        .willReturn(ofNullable(null));
//
//        assertThatThrownBy(() -> paymentGetService.byUserAndOrderId(any(User.class), anyString()))
//                .isInstanceOf(PaymentNotFoundException.class);
//    }
//
//    @Test
//    @DisplayName("payment 조회 테스트")
//    void byOrderId() {
//        Payment payment = mock(Payment.class);
//
//        given(paymentRepository.findByUserAndOrderIdAndStatus(any(), any(), any()))
//                .willReturn(of(payment));
//
//        assertThat(paymentGetService.byUserAndOrderId(any(), any()))
//                .isEqualTo(payment);
//    }
//}