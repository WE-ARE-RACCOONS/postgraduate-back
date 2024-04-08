package com.postgraduate.domain.salary.application.usecase;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
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

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SalaryInfoUseCase {
    private final SeniorGetService seniorGetService;
    private final SalaryGetService salaryGetService;
    private final MentoringGetService mentoringGetService;

    public SalaryInfoResponse getSalary(User user) {
        Senior senior = seniorGetService.byUser(user);
        Salary salary = salaryGetService.bySenior(senior);
        int amount = salary.getTotalAmount();
        return new SalaryInfoResponse(salary.getSalaryDate(), amount);
    }

    public SalaryDetailsResponse getSalaryDetail(User user, Boolean status) {
        Senior senior = seniorGetService.byUser(user);
        List<Mentoring> mentorings = status ?
                        mentoringGetService.bySeniorAndSalaryTrue(senior) :
                        mentoringGetService.bySeniorAndSalaryFalse(senior);
        List<SalaryDetails> salaryDetails = mentorings.stream()
                .map(SalaryMapper::mapToSalaryDetail)
                .toList();
        return new SalaryDetailsResponse(salaryDetails);
    }
}
