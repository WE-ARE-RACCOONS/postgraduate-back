package com.postgraduate.domain.salary.application.usecase;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.entity.constant.Status;
import com.postgraduate.domain.salary.application.dto.res.SalaryDetailsResponse;
import com.postgraduate.domain.salary.application.dto.res.SalaryInfoResponse;
import com.postgraduate.domain.salary.application.mapper.SalaryMapper;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.entity.SalaryAccount;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.user.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.DONE;
import static com.postgraduate.domain.salary.util.SalaryUtil.getSalaryDate;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;

@ExtendWith(MockitoExtension.class)
class CreateSalaryUseTypeTest {
    @Mock
    private SeniorGetService seniorGetService;
    @Mock
    private SalaryGetService salaryGetService;
    @Mock
    private MentoringGetService mentoringGetService;
    @Mock
    private SalaryMapper salaryMapper;
    @InjectMocks
    private SalaryInfoUseCase salaryInfoUseCase;

    private User user;
    private Senior senior;
    private Salary salary;
    private SalaryAccount salaryAccount;
    private LocalDate salaryDate;

    @BeforeEach
    void setting() {
        user = mock(User.class);
        senior = mock(Senior.class);
        salaryAccount = new SalaryAccount("bank", "1234", "holder");
        salaryDate = getSalaryDate();
        salary = new Salary(1L, FALSE, senior, 10000, salaryDate, null, salaryAccount);
    }
    @Test
    @DisplayName("정산 예정 금액 및 날짜 확인")
    void getSalary() {
        given(seniorGetService.byUser(user))
                .willReturn(senior);
        given(salaryGetService.bySenior(senior))
                .willReturn(salary);

        SalaryInfoResponse salaryInfoResponse = salaryInfoUseCase.getSalary(user);

        assertThat(salaryInfoResponse.salaryAmount())
                .isEqualTo(salary.getTotalAmount());
        assertThat(salaryInfoResponse.salaryDate())
                .isEqualTo(salaryDate);
    }

    @Test
    @DisplayName("미정산 내역 확인")
    void getNotDoneSalaryDetail() {
        Salary salary = mock(Salary.class);

        Payment payment1 = new Payment(1L, user, senior, 1000, "a", "a", "a", LocalDateTime.now(), LocalDateTime.now(), Status.DONE);
        Payment payment2 = new Payment(2L, user, senior, 1000, "a", "a", "a", LocalDateTime.now(), LocalDateTime.now(), Status.DONE);
        Payment payment3 = new Payment(3L, user, senior, 1000, "a", "a", "a", LocalDateTime.now(), LocalDateTime.now(), Status.DONE);

        Mentoring mentoring1 = new Mentoring(1L, user, senior, payment1, salary
                , "a", "b", "c"
                , 40,  DONE
                , LocalDateTime.now(), LocalDateTime.now());
        Mentoring mentoring2 = new Mentoring(2L, user, senior, payment2, salary
                , "a", "b", "c"
                , 40,  DONE
                , LocalDateTime.now(), LocalDateTime.now());
        Mentoring mentoring3 = new Mentoring(3L, user, senior, payment3, salary
                , "a", "b", "c"
                , 40,  DONE
                , LocalDateTime.now(), LocalDateTime.now());
        List<Mentoring> mentorings = List.of(mentoring1, mentoring2, mentoring3);

        given(seniorGetService.byUser(user))
                .willReturn(senior);
        given(mentoringGetService.bySeniorAndSalaryFalse(senior))
                .willReturn(mentorings);

        SalaryDetailsResponse salaryDetail = salaryInfoUseCase.getSalaryDetail(user, FALSE);

        assertThat(salaryDetail.salaryDetails())
                .hasSameSizeAs(mentorings);
    }

    @Test
    @DisplayName("정산완료 내역 확인")
    void getDoneSalaryDetail() {
        Salary salary = mock(Salary.class);

        Payment payment1 = new Payment(1L, user, senior, 1000, "a", "a", "a", LocalDateTime.now(), LocalDateTime.now(), Status.DONE);
        Payment payment2 = new Payment(2L, user, senior, 1000, "a", "a", "a", LocalDateTime.now(), LocalDateTime.now(), Status.DONE);
        Payment payment3 = new Payment(3L, user, senior, 1000, "a", "a", "a", LocalDateTime.now(), LocalDateTime.now(), Status.DONE);

        Mentoring mentoring1 = new Mentoring(1L, user, senior, payment1, salary
                , "a", "b", "c"
                , 40,  DONE
                , LocalDateTime.now(), LocalDateTime.now());
        Mentoring mentoring2 = new Mentoring(2L, user, senior, payment2, salary
                , "a", "b", "c"
                , 40,  DONE
                , LocalDateTime.now(), LocalDateTime.now());
        Mentoring mentoring3 = new Mentoring(3L, user, senior, payment3, salary
                , "a", "b", "c"
                , 40,  DONE
                , LocalDateTime.now(), LocalDateTime.now());
        List<Mentoring> mentorings = List.of(mentoring1, mentoring2, mentoring3);

        given(seniorGetService.byUser(user))
                .willReturn(senior);
        given(mentoringGetService.bySeniorAndSalaryTrue(senior))
                .willReturn(mentorings);

        SalaryDetailsResponse salaryDetail = salaryInfoUseCase.getSalaryDetail(user, TRUE);

        assertThat(salaryDetail.salaryDetails())
                .hasSameSizeAs(mentorings);
    }
}