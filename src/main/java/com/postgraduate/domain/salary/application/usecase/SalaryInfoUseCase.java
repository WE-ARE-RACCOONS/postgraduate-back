package com.postgraduate.domain.salary.application.usecase;

import com.postgraduate.domain.salary.application.dto.res.SalaryDetailResponse;
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
import java.util.List;

import static com.postgraduate.domain.salary.util.SalaryUtil.getAmount;
import static com.postgraduate.domain.salary.util.SalaryUtil.getSalaryDate;

@Service
@Transactional
@RequiredArgsConstructor
public class SalaryInfoUseCase {
    private final SeniorGetService seniorGetService;
    private final SalaryGetService salaryGetService;

    public SalaryInfoResponse getSalary(User user) {
        Senior senior = seniorGetService.byUser(user);
        LocalDate salaryDate = getSalaryDate();
        List<Salary> salaries = salaryGetService.bySeniorAndSalaryDate(senior, salaryDate);
        int amount = getAmount(salaries);
        return new SalaryInfoResponse(salaryDate, amount); //TODO 수수료
    }

    public List<SalaryDetailResponse> getSalaryDetail(User user, Boolean status) {
        Senior senior = seniorGetService.byUser(user);
        List<Salary> salaries = salaryGetService.bySeniorAndStatus(senior, status);
        return salaries.stream()
                .map(SalaryMapper::mapToSalaryDetail)
                .toList();
    }
}
