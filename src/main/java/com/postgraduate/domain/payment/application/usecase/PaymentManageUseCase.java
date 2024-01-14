package com.postgraduate.domain.payment.application.usecase;

import com.postgraduate.domain.admin.application.dto.req.PaymentResultRequest;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.payment.application.mapper.PaymentMapper;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.service.PaymentSaveService;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentManageUseCase {
    private final PaymentSaveService paymentSaveService;
    private final MentoringGetService mentoringGetService;
    private final SalaryGetService salaryGetService;

    public void savePay(PaymentResultRequest paymentResultRequest) {
        long mentoringId = Long.parseLong(paymentResultRequest.PCD_PAY_GOODS());
        Mentoring mentoring = mentoringGetService.byMentoringId(mentoringId);
        Salary salary = salaryGetService.bySenior(mentoring.getSenior());
        Payment payment = PaymentMapper.resultToPayment(mentoring, salary, paymentResultRequest);
        paymentSaveService.save(payment);
    }
}
