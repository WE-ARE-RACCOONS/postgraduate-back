package com.postgraduate.batch.done;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.domain.member.senior.domain.entity.Senior;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DoneMentoringProcessor implements ItemProcessor<Mentoring, DoneMentoring> {
    private final SalaryGetService salaryGetService;

    @Override
    public DoneMentoring process(Mentoring mentoring) {
        if (mentoring.checkAutoDone()) {
            Senior senior = mentoring.getSenior();
            Payment payment = mentoring.getPayment();
            Salary salary = salaryGetService.bySenior(senior);
            return new DoneMentoring(mentoring.getMentoringId(), senior.getSeniorId(), salary.getSalaryId(), mentoring.getDate(), payment.getPay());
        }
        return null;
    }
}