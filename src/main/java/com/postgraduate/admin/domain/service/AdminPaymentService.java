package com.postgraduate.admin.domain.service;

import com.postgraduate.admin.application.dto.res.PaymentWithMentoringQuery;
import com.postgraduate.admin.domain.repository.AdminPaymentRepository;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.domain.mentoring.exception.MentoringNotFoundException;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.exception.PaymentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.CANCEL;

@Service
@RequiredArgsConstructor
public class AdminPaymentService {
    private final AdminPaymentRepository adminPaymentRepository;

    public List<PaymentWithMentoringQuery> allPayments() {
        return adminPaymentRepository.findAllPayment();
    }
}
