package com.postgraduate.domain.salary.application.usecase;

import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.service.PaymentGetService;
import com.postgraduate.domain.salary.application.dto.SalaryDetails;
import com.postgraduate.domain.salary.application.dto.res.SalaryDetailsResponse;
import com.postgraduate.domain.salary.application.dto.res.SalaryInfoResponse;
import com.postgraduate.domain.salary.application.mapper.SalaryMapper;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.postgraduate.domain.salary.util.SalaryUtil.getSalaryDate;

@Service
@Transactional
@RequiredArgsConstructor
public class SalaryInfoUseCase {
    private final SeniorGetService seniorGetService;
    private final SalaryGetService salaryGetService;
    private final PaymentGetService paymentGetService;

    public SalaryInfoResponse getSalary(User user) {
        Senior senior = seniorGetService.byUser(user);
        LocalDate salaryDate = getSalaryDate();
        Salary salary = salaryGetService.bySenior(senior);
        int amount = salary.getTotalAmount();
        return new SalaryInfoResponse(salaryDate, amount); //TODO 수수료
    }

    public SalaryDetailsResponse getSalaryDetail(User user, Boolean status) {
        Senior senior = seniorGetService.byUser(user);
        List<Payment> payments = paymentGetService.bySeniorAndStatus(senior, status);
//        List<Salary> salaries = salaryGetService.bySeniorAndStatus(senior, status);
//        List<SalaryDetails> salaryDetails = new ArrayList<>();
//        salaries.forEach(salary -> {
//                    List<Payment> payments = salary.getPayments();
//                    payments.forEach(payment -> {
//                        salaryDetails.add(SalaryMapper.mapToSalaryDetail(salary, payment));
//                    });
//                });
        List<SalaryDetails> salaryDetails = payments.stream().
                map(payment -> {
                    return SalaryMapper.mapToSalaryDetail(payment.getSalary(), payment);
                }).toList();
        return new SalaryDetailsResponse(salaryDetails);
    }
}
