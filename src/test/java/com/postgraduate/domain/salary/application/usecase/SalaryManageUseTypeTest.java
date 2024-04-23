package com.postgraduate.domain.salary.application.usecase;

import com.postgraduate.domain.salary.application.dto.SeniorAndAccount;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.global.slack.SlackSalaryMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalaryManageUseTypeTest {
    @Mock
    private SalaryGetService salaryGetService;
    @Mock
    private SeniorGetService seniorGetService;
    @Mock
    private SlackSalaryMessage slackSalaryMessage;
    @Mock
    private SalaryRenewalUseCase salaryRenewalUseCase;
    @InjectMocks
    private SalaryManageUseCase salaryManageUseCase;

    @Test
    @DisplayName("정산 자동 생성 테스트")
    void createSalary() {
        List<SeniorAndAccount> seniorAndAccounts = List.of(mock(SeniorAndAccount.class), mock(SeniorAndAccount.class), mock(SeniorAndAccount.class));
        given(seniorGetService.findAllSeniorAndAccount())
                .willReturn(seniorAndAccounts);
        salaryManageUseCase.createSalary();

        verify(salaryRenewalUseCase, times(seniorAndAccounts.size()))
                .createSalaryWithAuto(any(), any());
    }
}
