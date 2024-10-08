package com.postgraduate.domain.payment.domain.service;

import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.repository.PaymentRepository;
import com.postgraduate.domain.payment.exception.PaymentNotFoundException;
import com.postgraduate.domain.member.senior.domain.entity.Senior;
import com.postgraduate.domain.member.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.postgraduate.domain.payment.domain.entity.constant.Status.DONE;

@Service
@RequiredArgsConstructor
public class PaymentGetService {
    private final PaymentRepository paymentRepository;

    public Payment byId(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(PaymentNotFoundException::new);
    }

    public Payment byUserAndOrderId(User user, String orderId) {
        return paymentRepository.findByUserAndOrderIdAndStatus(user, orderId, DONE)
                .orElseThrow(PaymentNotFoundException::new);
    }

    public Payment byUserIdAndOrderId(Long userId, Long paymentId) {
        return paymentRepository.findByUser_UserIdAndPaymentIdAndStatus(userId, paymentId, DONE)
                .orElseThrow(PaymentNotFoundException::new);
    }

    public Payment bySeniorAndOrderId(Senior senior, String orderId) {
        return paymentRepository.findBySeniorAndOrderIdAndStatus(senior, orderId, DONE).orElseThrow(PaymentNotFoundException::new);
    }
}
