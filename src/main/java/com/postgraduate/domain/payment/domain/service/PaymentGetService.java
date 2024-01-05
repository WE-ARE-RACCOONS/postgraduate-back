package com.postgraduate.domain.payment.domain.service;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.repository.PaymentRepository;
import com.postgraduate.domain.payment.exception.PaymentNotFoundException;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.senior.domain.entity.Senior;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentGetService {
    private static final int ADMIN_PAGE_SIZE = 15;
    private final PaymentRepository paymentRepository;
    public Page<Payment> all(Integer page, String search) {
        page = page == null ? 1 : page;
        Pageable pageable = PageRequest.of(page - 1, ADMIN_PAGE_SIZE);
        return paymentRepository.findAllBySearchPayment(search, pageable);
    }

    public Payment byMentoring(Mentoring mentoring) {
        return paymentRepository.findByMentoring(mentoring).orElseThrow(PaymentNotFoundException::new);
    }

    public List<Payment> bySalary(Salary salary) {
        return paymentRepository.findAllBySalary(salary);
    }

    public List<Payment> bySeniorAndStatus(Senior senior, Boolean status) {
        return paymentRepository.findAllBySeniorAndStatus(senior, status);
    }
}
