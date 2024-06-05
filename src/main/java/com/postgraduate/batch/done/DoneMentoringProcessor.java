package com.postgraduate.batch.done;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.senior.domain.entity.Senior;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.LocalDateTime.now;
import static java.time.LocalDateTime.parse;
import static java.time.format.DateTimeFormatter.ofPattern;

@Component
public class DoneMentoringProcessor implements ItemProcessor<Mentoring, DoneMentoring> {
    @Override
    public DoneMentoring process(Mentoring mentoring) {
        DateTimeFormatter formatter = ofPattern("yyyy-MM-dd-HH-mm");
        LocalDateTime doneDate = parse(mentoring.getDate(), formatter);
        if (now().minusDays(3).isAfter(doneDate)) {
            Senior senior = mentoring.getSenior();
            Salary salary = mentoring.getSalary();
            Payment payment = mentoring.getPayment();
            return new DoneMentoring(mentoring.getMentoringId(), senior.getSeniorId(), salary.getSalaryId(), mentoring.getDate(), payment.getPay());
        }
        return null;
    }
}
