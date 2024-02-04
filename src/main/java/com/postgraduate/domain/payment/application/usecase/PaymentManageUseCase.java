package com.postgraduate.domain.payment.application.usecase;

import com.postgraduate.domain.payment.application.dto.req.PaymentResultRequest;
import com.postgraduate.domain.payment.application.dto.req.PaymentResultWithMentoringRequest;
import com.postgraduate.domain.payment.application.mapper.PaymentMapper;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.service.PaymentSaveService;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.service.UserGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentManageUseCase {
    private final PaymentSaveService paymentSaveService;
    private final SeniorGetService seniorGetService;
    private final UserGetService userGetService;
    private final SalaryGetService salaryGetService;

    public Payment savePay(PaymentResultRequest paymentResultRequest) {
        check();
        long seniorId = Long.parseLong(paymentResultRequest.PCD_PAY_GOODS());
        long userId = Long.parseLong(paymentResultRequest.PCD_PAYER_NO());
        User user = userGetService.byUserId(userId);
        Senior senior = seniorGetService.bySeniorId(seniorId);
        Salary salary = salaryGetService.bySenior(senior);
        Payment payment = PaymentMapper.resultToPayment(salary, user, paymentResultRequest);
        return paymentSaveService.save(payment);
    }

    public Payment savePay(PaymentResultWithMentoringRequest paymentResultRequest) {
        check();
        long seniorId = Long.parseLong(paymentResultRequest.PCD_PAY_GOODS());
        long userId = Long.parseLong(paymentResultRequest.PCD_PAYER_NO());
        User user = userGetService.byUserId(userId);
        Senior senior = seniorGetService.bySeniorId(seniorId);
        Salary salary = salaryGetService.bySenior(senior);
        Payment payment = PaymentMapper.resultToPayment(salary, user, paymentResultRequest);
        return paymentSaveService.save(payment);
    }

    private void check() {
        // todo payple에 유효성 검사
    }
}
