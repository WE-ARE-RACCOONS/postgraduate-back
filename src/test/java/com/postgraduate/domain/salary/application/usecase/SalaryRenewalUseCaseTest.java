package com.postgraduate.domain.salary.application.usecase;

import com.postgraduate.domain.salary.application.dto.SeniorAndAccount;
import com.postgraduate.domain.salary.domain.service.SalarySaveService;
import com.postgraduate.domain.salary.util.SalaryUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalaryRenewalUseCaseTest {
    @Mock
    private SalarySaveService salarySaveService;
    @InjectMocks
    private SalaryRenewalUseCase salaryRenewalUseCase;

    @Test
    @DisplayName("정산 생성 테스트")
    void createSalary() {
        LocalDate salaryDate = SalaryUtil.getSalaryDate();
        SeniorAndAccount seniorAndAccount = mock(SeniorAndAccount.class);
        salaryRenewalUseCase.createSalaryWithAuto(seniorAndAccount, salaryDate);
        verify(salarySaveService)
                .save(any());
    }
}
