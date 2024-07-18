package com.postgraduate.domain.salary.presentation;

import com.postgraduate.domain.salary.application.dto.SalaryDetails;
import com.postgraduate.domain.salary.application.dto.res.SalaryDetailsResponse;
import com.postgraduate.domain.salary.application.dto.res.SalaryInfoResponse;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.user.user.domain.entity.User;
import com.postgraduate.support.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.util.List;

import static com.postgraduate.domain.salary.presentation.constant.SalaryResponseCode.SALARY_FIND;
import static com.postgraduate.domain.salary.presentation.constant.SalaryResponseMessage.GET_SALARY_INFO;
import static com.postgraduate.domain.salary.presentation.constant.SalaryResponseMessage.GET_SALARY_LIST_INFO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SalaryControllerTest extends ControllerTest {
    private static final String BEARER = "Bearer token";
    
    Salary salary = resource.getSalary();
    User user = resource.getUser();
    
    @Test
    @WithMockUser("SENIOR")
    @DisplayName("대학원생 정산 예정액과 다음 정산 예정일을 조회한다")
    void getSalary() throws Exception {
        SalaryInfoResponse response = new SalaryInfoResponse(salary.getSalaryDate(), salary.getTotalAmount());
        given(salaryInfoUseCase.getSalary(any()))
                .willReturn(response);
        
        mvc.perform(get("/salary")
                        .header(HttpHeaders.AUTHORIZATION, BEARER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SALARY_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_SALARY_INFO.getMessage()))
                .andExpect(jsonPath("$.data.salaryDate").value(response.salaryDate().toString()))
                .andExpect(jsonPath("$.data.salaryAmount").value(response.salaryAmount()));
    }

    @Test
    @WithMockUser("SENIOR")
    @DisplayName("대학원생 정산예정 목록을 조회한다")
    void getWaitingSalary() throws Exception {
        SalaryDetails salaryDetails = new SalaryDetails(user.getProfile(), user.getNickName(), "date", 30, 10000, LocalDate.now());
        SalaryDetailsResponse response = new SalaryDetailsResponse(List.of(salaryDetails, mock(SalaryDetails.class), mock(SalaryDetails.class)));

        given(salaryInfoUseCase.getSalaryDetail(any(), any()))
                .willReturn(response);

        mvc.perform(get("/salary/waiting")
                        .header(HttpHeaders.AUTHORIZATION, BEARER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SALARY_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_SALARY_LIST_INFO.getMessage()))
                .andExpect(jsonPath("$.data.salaryDetails[0].profile").value(salaryDetails.profile()))
                .andExpect(jsonPath("$.data.salaryDetails[0].nickName").value(salaryDetails.nickName()))
                .andExpect(jsonPath("$.data.salaryDetails[0].date").value(salaryDetails.date()))
                .andExpect(jsonPath("$.data.salaryDetails[0].term").value(salaryDetails.term()))
                .andExpect(jsonPath("$.data.salaryDetails[0].salaryAmount").value(salaryDetails.salaryAmount()))
                .andExpect(jsonPath("$.data.salaryDetails[0].salaryDate").value(salaryDetails.salaryDate().toString()));
    }

    @Test
    @WithMockUser("SENIOR")
    @DisplayName("대학원생 정산완료 목록을 조회한다")
    void getDoneSalary() throws Exception {
        SalaryDetails salaryDetails = new SalaryDetails(user.getProfile(), user.getNickName(), "date", 30, 10000, LocalDate.now());
        SalaryDetailsResponse response = new SalaryDetailsResponse(List.of(salaryDetails, mock(SalaryDetails.class), mock(SalaryDetails.class)));

        given(salaryInfoUseCase.getSalaryDetail(any(), any()))
                .willReturn(response);

        mvc.perform(get("/salary/done")
                        .header(HttpHeaders.AUTHORIZATION, BEARER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SALARY_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_SALARY_LIST_INFO.getMessage()))
                .andExpect(jsonPath("$.data.salaryDetails[0].profile").value(salaryDetails.profile()))
                .andExpect(jsonPath("$.data.salaryDetails[0].nickName").value(salaryDetails.nickName()))
                .andExpect(jsonPath("$.data.salaryDetails[0].date").value(salaryDetails.date()))
                .andExpect(jsonPath("$.data.salaryDetails[0].term").value(salaryDetails.term()))
                .andExpect(jsonPath("$.data.salaryDetails[0].salaryAmount").value(salaryDetails.salaryAmount()))
                .andExpect(jsonPath("$.data.salaryDetails[0].salaryDate").value(salaryDetails.salaryDate().toString()));
    }
}