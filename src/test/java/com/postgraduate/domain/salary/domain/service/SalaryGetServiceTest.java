package com.postgraduate.domain.salary.domain.service;

import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.repository.SalaryRepository;
import com.postgraduate.domain.salary.exception.SalaryNotFoundException;
import com.postgraduate.domain.salary.util.SalaryUtil;
import com.postgraduate.domain.senior.domain.entity.Senior;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class SalaryGetServiceTest {
    @Mock
    private SalaryRepository salaryRepository;
    @InjectMocks
    private SalaryGetService salaryGetService;

    private Salary salary = mock(Salary.class);
    private Senior senior = mock(Senior.class);

    @Test
    @DisplayName("salaryId를 통한 Salary 조회 테스트")
    void bySalaryId() {
        given(salaryRepository.findById(any()))
                .willReturn(Optional.of(salary));

        assertThat(salaryGetService.bySalaryId(any()))
                .isEqualTo(salary);
    }

    @Test
    @DisplayName("salaryId를 통한 Salary 조회 예외 테스트")
    void bySalaryIdFail() {
        given(salaryRepository.findById(any()))
                .willReturn(Optional.ofNullable(null));

        assertThatThrownBy(() -> salaryGetService.bySalaryId(any()))
                .isInstanceOf(SalaryNotFoundException.class);
    }

    @Test
    @DisplayName("SeniorId 통한 Salary 조회 테스트")
    void bySeniorId() {
        LocalDate salaryDate = SalaryUtil.getSalaryDate();
        given(salaryRepository.findBySeniorAndSalaryDateAndSenior_User_IsDeleteIsFalse(senior, salaryDate))
                .willReturn(Optional.of(salary));

        assertThat(salaryGetService.bySenior(senior))
                .isEqualTo(salary);
    }

    @Test
    @DisplayName("SeniorId 통한 Salary 조회 예외 테스트")
    void bySeniorIdFail() {
        LocalDate salaryDate = SalaryUtil.getSalaryDate();
        given(salaryRepository.findBySeniorAndSalaryDateAndSenior_User_IsDeleteIsFalse(senior, salaryDate))
                .willThrow(SalaryNotFoundException.class);

        assertThatThrownBy(() -> salaryGetService.bySenior(senior))
                .isInstanceOf(SalaryNotFoundException.class);
    }
}