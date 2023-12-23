package com.postgraduate.domain.salary.application.usecase;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.salary.application.dto.res.SalaryDetailsResponse;
import com.postgraduate.domain.salary.application.dto.res.SalaryInfoResponse;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.domain.salary.util.SalaryUtil;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.user.domain.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.DONE;
import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.WAITING;
import static com.postgraduate.domain.salary.util.SalaryUtil.*;
import static java.lang.Boolean.FALSE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalaryInfoUseCaseTest {
    @Mock
    private SeniorGetService seniorGetService;
    @Mock
    private SalaryGetService salaryGetService;
    @InjectMocks
    private SalaryInfoUseCase salaryInfoUseCase;

    private User user;
    private Senior senior;

    @BeforeEach
    void setting() {
        user = mock(User.class);
        senior = mock(Senior.class);
    }
    @Test
    @DisplayName("정산 예정 금액 및 날짜 확인")
    void getSalary() {
        LocalDate salaryDate = getSalaryDate();
        Mentoring mentoring = new Mentoring(1L, user, senior
                , "a", "b", "c"
                , 40, 40, DONE
                , LocalDate.now(), LocalDate.now());
        Salary salary1 = new Salary(1L, FALSE, senior, mentoring, salaryDate, null);
        Salary salary2 = new Salary(2L, FALSE, senior, mentoring, salaryDate, null);
        Salary salary3 = new Salary(3L, FALSE, senior, mentoring, salaryDate, null);
        List<Salary> salaries = List.of(salary1, salary2, salary3);
        int amount = getAmount(salaries);

        given(seniorGetService.byUser(user))
                .willReturn(senior);
        given(salaryGetService.bySeniorAndSalaryDate(senior, salaryDate))
                .willReturn(salaries);

        SalaryInfoResponse salary = salaryInfoUseCase.getSalary(user);

        assertThat(salary.getSalaryAmount())
                .isEqualTo(amount);
        assertThat(salary.getSalaryDate())
                .isEqualTo(salaryDate);
    }

    @Test
    @DisplayName("정산 예정 금액 및 날짜 확인 - 0원")
    void getSalaryWithZero() {
        LocalDate salaryDate = getSalaryDate();
        List<Salary> salaries = List.of();

        given(seniorGetService.byUser(user))
                .willReturn(senior);
        given(salaryGetService.bySeniorAndSalaryDate(senior, salaryDate))
                .willReturn(salaries);

        SalaryInfoResponse salary = salaryInfoUseCase.getSalary(user);

        assertThat(salary.getSalaryAmount())
                .isEqualTo(0);
        assertThat(salary.getSalaryDate())
                .isEqualTo(salaryDate);
    }

    @Test
    @DisplayName("정산 내역 확인")
    void getSalaryDetail() {
        LocalDate salaryDate = getSalaryDate();
        Mentoring mentoring = new Mentoring(1L, user, senior
                , "a", "b", "c"
                , 40, 40, DONE
                , LocalDate.now(), LocalDate.now());
        Salary salary1 = new Salary(1L, FALSE, senior, mentoring, salaryDate, null);
        Salary salary2 = new Salary(2L, FALSE, senior, mentoring, salaryDate, null);
        Salary salary3 = new Salary(3L, FALSE, senior, mentoring, salaryDate, null);
        List<Salary> salaries = List.of(salary1, salary2, salary3);

        given(seniorGetService.byUser(user))
                .willReturn(senior);
        given(salaryGetService.bySeniorAndStatus(senior, FALSE))
                .willReturn(salaries);

        SalaryDetailsResponse salaryDetail = salaryInfoUseCase.getSalaryDetail(user, FALSE);

        assertThat(salaryDetail.salaryDetails().size())
                .isEqualTo(salaries.size());
    }
}