package com.postgraduate.admin.application.usecase;

import com.postgraduate.admin.application.dto.res.MentoringWithPaymentResponse;
import com.postgraduate.admin.application.dto.res.PaymentInfo;
import com.postgraduate.admin.application.mapper.AdminMapper;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.mentoring.domain.service.MentoringUpdateService;
import com.postgraduate.domain.payment.application.usecase.PaymentManageUseCase;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.service.PaymentGetService;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.domain.salary.domain.service.SalaryUpdateService;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminPaymentUseCase {
    private final PaymentGetService paymentGetService;
    private final MentoringGetService mentoringGetService;
    private final MentoringUpdateService mentoringUpdateService;
    private final PaymentManageUseCase paymentManageUseCase;
    private final SalaryGetService salaryGetService;
    private final SalaryUpdateService salaryUpdateService;
    private final AdminMapper adminMapper;

    @Transactional(readOnly = true)
    public List<PaymentInfo> paymentInfos() {
        List<Payment> all = paymentGetService.all();
        return all.stream()
                .map(payment -> {
                    Mentoring mentoring = mentoringGetService.byPaymentWithNull(payment);
                    if (mentoring == null)
                        return adminMapper.mapToPaymentInfo(payment);
                    return adminMapper.mapToPaymentInfo(payment, mentoring);
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public MentoringWithPaymentResponse paymentMentoringInfo(Long paymentId) {
        Payment payment = paymentGetService.byId(paymentId);
        Mentoring mentoring = mentoringGetService.byPayment(payment);
        return adminMapper.mapToMentoringWithPaymentResponse(mentoring);
    }

    public void refundPayment(User user, Long paymentId) {
        paymentManageUseCase.refundPayByAdmin(user, paymentId);
        Payment payment = paymentGetService.byId(paymentId);
        Mentoring mentoring = mentoringGetService.byPaymentWithNull(payment);
        if (mentoring != null) {
            mentoringUpdateService.updateCancel(mentoring);
            Senior senior = mentoring.getSenior();
            Salary salary = salaryGetService.bySenior(senior);
            salaryUpdateService.minusTotalAmount(salary, mentoring.calculateForSenior());
        }
    }
}
