package com.postgraduate.domain.salary.domain.service;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
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

import java.util.Optional;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class SalaryGetServiceTest {
    @Mock
    private SalaryRepository salaryRepository;
    @InjectMocks
    private SalaryGetService salaryGetService;

    @Test
    @DisplayName("Senior를 통한 Salary 조회 예외 테스트")
    void bySeniorFailTest() {
        Senior senior = mock(Senior.class);
        given(salaryRepository.findBySeniorAndSalaryDate(senior, SalaryUtil.getSalaryDate()))
                .willReturn(ofNullable(null));

        assertThatThrownBy(() -> salaryGetService.bySenior(senior))
                .isInstanceOf(SalaryNotFoundException.class);
    }

    @Test
    @DisplayName("Senior를 통한 Salary 조회 테스트")
    void byMentoring() {
        Senior senior = mock(Senior.class);
        Salary salary = mock(Salary.class);
        given(salaryRepository.findBySeniorAndSalaryDate(senior, SalaryUtil.getSalaryDate()))
                .willReturn(Optional.of(salary));

        assertThat(salaryGetService.bySenior(senior))
                .isEqualTo(salary);
    }
}