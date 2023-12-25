package com.postgraduate.domain.salary.domain.service;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.senior.domain.entity.Senior;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class SalaryUpdateServiceTest {
    @InjectMocks
    private SalaryUpdateService salaryUpdateService;

    @Test
    @DisplayName("정산 완료 테스트")
    void updateStatusTRUE() {
        Senior senior = mock(Senior.class);
        Mentoring mentoring = mock(Mentoring.class);
        Salary salary = new Salary(1L, FALSE, senior, mentoring, LocalDate.now(), LocalDateTime.now());

        salaryUpdateService.updateStatus(salary, TRUE);

        assertThat(salary.getStatus())
                .isTrue();
    }

    @Test
    @DisplayName("정산 미완료 테스트")
    void updateStatusFALSE() {
        Senior senior = mock(Senior.class);
        Mentoring mentoring = mock(Mentoring.class);
        Salary salary = new Salary(1L, TRUE, senior, mentoring, LocalDate.now(), LocalDateTime.now());

        salaryUpdateService.updateStatus(salary, FALSE);

        assertThat(salary.getStatus())
                .isFalse();
    }
}