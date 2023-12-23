package com.postgraduate.domain.salary.domain.service;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.repository.SalaryRepository;
import com.postgraduate.domain.salary.exception.SalaryNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    @DisplayName("정산 조회 예외 테스트")
    void byMentoringFail() {
        Mentoring mentoring = mock(Mentoring.class);
        given(salaryRepository.findByMentoring(mentoring))
                .willReturn(ofNullable(null));

        assertThatThrownBy(() -> salaryGetService.byMentoring(mentoring))
                .isInstanceOf(SalaryNotFoundException.class);
    }

    @Test
    @DisplayName("정산 조회 테스트")
    void byMentoring() {
        Mentoring mentoring = mock(Mentoring.class);
        Salary salary = mock(Salary.class);
        given(salaryRepository.findByMentoring(mentoring))
                .willReturn(of(salary));

        assertThat(salaryGetService.byMentoring(mentoring))
                .isEqualTo(salary);
    }
}