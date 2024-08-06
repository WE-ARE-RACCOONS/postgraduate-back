package com.postgraduate.domain.payment.application.mapper;

import com.postgraduate.domain.payment.application.dto.req.PaymentResultRequest;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.user.domain.entity.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PaymentMapper {
    private PaymentMapper() {
        throw new IllegalArgumentException();
    }

    public static Payment resultToPayment(Senior senior, User user, PaymentResultRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return Payment.builder()
                .user(user)
                .senior(senior)
                .pay(Integer.parseInt(request.PCD_PAY_TOTAL()))
                .orderId(request.PCD_PAY_OID())
                .cardAuthNumber(request.PCD_PAY_CARDAUTHNO())
                .cardReceipt(request.PCD_PAY_CARDRECEIPT())
                .paidAt(LocalDateTime.parse(request.PCD_PAY_TIME(), formatter))
                .build();
    }

    public static Payment resultToPayment(PaymentResultRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return Payment.builder()
                .pay(Integer.parseInt(request.PCD_PAY_TOTAL()))
                .orderId(request.PCD_PAY_OID())
                .cardAuthNumber(request.PCD_PAY_CARDAUTHNO())
                .cardReceipt(request.PCD_PAY_CARDRECEIPT())
                .paidAt(LocalDateTime.parse(request.PCD_PAY_TIME(), formatter))
                .build();
    }
}
