package com.postgraduate.domain.payment.application.mapper;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.payment.application.dto.req.PaymentResultRequest;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.salary.domain.entity.Salary;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PaymentMapper {
    public static Payment resultToPayment(Mentoring mentoring, Salary salary, PaymentResultRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return Payment.builder()
                .mentoring(mentoring)
                .salary(salary)
                .pay(Integer.parseInt(request.PCD_PAY_TOTAL()))
                .cardAuthNumber(request.PCD_PAY_CARDAUTHNO())
                .cardReceipt(request.PCD_PAY_CARDRECEIPT())
                .paidAt(LocalDateTime.parse(request.PCD_PAY_TIME(), formatter))
                .build();
    }
}
