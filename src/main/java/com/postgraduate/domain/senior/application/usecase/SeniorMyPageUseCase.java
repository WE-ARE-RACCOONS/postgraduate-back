package com.postgraduate.domain.senior.application.usecase;

import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.domain.senior.application.dto.res.SeniorInfoResponse;
import com.postgraduate.domain.senior.application.dto.res.SeniorMyPageResponse;
import com.postgraduate.domain.senior.domain.entity.Profile;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.entity.constant.Status;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static com.postgraduate.domain.salary.util.MonthFormat.getMonthFormat;
import static com.postgraduate.domain.senior.application.mapper.SeniorMapper.mapToOriginInfo;
import static com.postgraduate.domain.senior.application.mapper.SeniorMapper.mapToSeniorMyPageInfo;
import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
@Transactional
public class SeniorMyPageUseCase {
    private final SeniorGetService seniorGetService;
    private final SalaryGetService salaryGetService;

    public SeniorMyPageResponse getSeniorInfo(User user) {
        Senior senior = seniorGetService.byUser(user);
        String month = LocalDate.now().format(getMonthFormat());
        Salary salary = salaryGetService.bySeniorAndMonth(senior, month)
                .orElse(new Salary());
        Status status = senior.getStatus();
        Optional<Profile> profile = ofNullable(senior.getProfile());
        return mapToSeniorMyPageInfo(senior, salary, month, status, profile.isPresent());
    }

    public SeniorInfoResponse getSeniorOriginInfo(User user) {
        Senior senior = seniorGetService.byUser(user);
        return mapToOriginInfo(senior);
    }
}
