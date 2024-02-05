package com.postgraduate.domain.payment.application.usecase;

import com.postgraduate.domain.payment.application.dto.req.PaymentResultRequest;
import com.postgraduate.domain.payment.application.mapper.PaymentMapper;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.service.PaymentSaveService;
import com.postgraduate.domain.payment.exception.PaymentFailException;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.domain.salary.domain.service.SalaryUpdateService;
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
    private static final String SUCCESS = "0000";

    private final PaymentSaveService paymentSaveService;
    private final SeniorGetService seniorGetService;
    private final UserGetService userGetService;
    private final SalaryGetService salaryGetService;
    private final SalaryUpdateService salaryUpdateService;

    public void savePay(PaymentResultRequest request) {
        if (!request.PCD_PAY_CODE().equals(SUCCESS))
            throw new PaymentFailException();
        String seniorNickName = request.PCD_PAY_GOODS();
        try {
            long userId = Long.parseLong(request.PCD_PAYER_NO());
            User user = userGetService.byUserId(userId);
            Senior senior = seniorGetService.bySeniorNickName(seniorNickName);
            Salary salary = salaryGetService.bySenior(senior);
            Payment payment = PaymentMapper.resultToPayment(salary, user, request);
            paymentSaveService.save(payment);
            salaryUpdateService.updateTotalAmount(salary);
        } catch (Exception e) {
            //todo : 환불로직
        }
    }
}
