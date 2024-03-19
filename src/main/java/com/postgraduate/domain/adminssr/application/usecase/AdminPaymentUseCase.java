package com.postgraduate.domain.adminssr.application.usecase;

import com.postgraduate.domain.adminssr.application.dto.res.*;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.payment.application.usecase.PaymentManageUseCase;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.service.PaymentGetService;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.domain.salary.domain.service.SalaryUpdateService;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.postgraduate.domain.adminssr.application.mapper.AdminSsrMapper.mapToMentoringWithPaymentResponse;
import static com.postgraduate.domain.adminssr.application.mapper.AdminSsrMapper.mapToPaymentInfo;
import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.DONE;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminPaymentUseCase {
    private final PaymentGetService paymentGetService;
    private final MentoringGetService mentoringGetService;
    private final PaymentManageUseCase paymentManageUseCase;
    private final SalaryGetService salaryGetService;
    private final SalaryUpdateService salaryUpdateService;

    public List<PaymentInfo> paymentInfos() {
        List<Payment> all = paymentGetService.all();
        return all.stream()
                .map(payment -> {
                    Mentoring mentoring = mentoringGetService.byPaymentWithNull(payment);
                    if (mentoring == null)
                        return mapToPaymentInfo(payment);
                    return mapToPaymentInfo(payment, mentoring);
                })
                .toList();
    }

    public MentoringWithPaymentResponse paymentMentoringInfo(Long paymentId) {
        Payment payment = paymentGetService.byId(paymentId);
        Mentoring mentoring = mentoringGetService.byPayment(payment);
        return mapToMentoringWithPaymentResponse(mentoring);
    }

    public void refundPayment(User user, Long paymentId) {
        paymentManageUseCase.refundPayByAdmin(user, paymentId);
        Payment payment = paymentGetService.byId(paymentId);
        Mentoring mentoring = mentoringGetService.byPaymentWithNull(payment);
        if (mentoring != null && mentoring.getStatus() == DONE) {
            Senior senior = mentoring.getSenior();
            Salary salary = salaryGetService.bySenior(senior);
            salaryUpdateService.minusTotalAmount(salary);
        }
    }
}
