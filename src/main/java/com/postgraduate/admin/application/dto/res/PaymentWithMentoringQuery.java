package com.postgraduate.admin.application.dto.res;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.payment.domain.entity.Payment;

import java.util.Optional;

public record PaymentWithMentoringQuery(Payment payment, Optional<Mentoring> mentoring) {}
