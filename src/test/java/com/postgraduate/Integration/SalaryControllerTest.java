package com.postgraduate.Integration;

import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.entity.SalaryAccount;
import com.postgraduate.domain.salary.util.SalaryUtil;
import com.postgraduate.domain.senior.domain.entity.Info;
import com.postgraduate.domain.senior.domain.entity.Profile;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.entity.constant.Status;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.entity.constant.Role;
import com.postgraduate.support.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.postgraduate.domain.salary.presentation.constant.SalaryResponseCode.SALARY_FIND;
import static com.postgraduate.domain.salary.presentation.constant.SalaryResponseCode.SALARY_NOT_FOUND;
import static com.postgraduate.domain.salary.presentation.constant.SalaryResponseMessage.*;
import static java.time.LocalDateTime.now;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SalaryControllerTest extends IntegrationTest {
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private String token;
    private Salary salary;


    @BeforeEach
    void setUp() throws IOException {
        User user = new User(0L, 1L, "mail", "후배", "011", "profile", 0, Role.SENIOR, true, now(), now(), false);
        userRepository.save(user);

        Info info = new Info("major", "postgradu", "교수님", "keyword1,keyword2", "랩실", "field", false, false, "field,keyword1,keyword2");
        Profile profile = new Profile("저는요", "한줄소개", "대상", "chatLink", 40);
        Senior senior = new Senior(0L, user, "certification", Status.APPROVE, 0, info, profile, now(), now());
        seniorRepository.save(senior);

        SalaryAccount salaryAccount = new SalaryAccount("bank", "1234", "holder");
        salary = new Salary(0L, false, senior, 0, SalaryUtil.getSalaryDate(), null, salaryAccount);
        salaryRepository.save(salary);

        token = jwtUtil.generateAccessToken(user.getUserId(), Role.SENIOR);

        doNothing().when(slackLogErrorMessage).sendSlackLog(any());
    }

    @Test
    @DisplayName("대학원생 정산 예정액과 다음 정산 예정일을 조회한다")
    void getSalary() throws Exception {
        mvc.perform(get("/salary")
                        .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SALARY_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_SALARY_INFO.getMessage()));
    }

    @Test
    @DisplayName("정산이 없다면 예외가 발생한다")
    void getEmptySalary() throws Exception {
        salaryRepository.delete(salary);

        mvc.perform(get("/salary")
                        .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SALARY_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(NOT_FOUND_SALARY.getMessage()));
    }

    @Test
    @DisplayName("대학원생 정산예정 목록을 조회한다")
    void getWaitingSalary() throws Exception {
        mvc.perform(get("/salary/waiting")
                        .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SALARY_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_SALARY_LIST_INFO.getMessage()));
    }

    @Test
    @DisplayName("대학원생 정산완료 목록을 조회한다")
    void getDoneSalary() throws Exception {
        mvc.perform(get("/salary/done")
                        .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SALARY_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_SALARY_LIST_INFO.getMessage()));
    }
}