package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.account.domain.entity.Account;
import com.postgraduate.domain.account.domain.service.AccountGetService;
import com.postgraduate.domain.mentoring.application.dto.req.MentoringDateRequest;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.mentoring.domain.service.MentoringUpdateService;
import com.postgraduate.domain.mentoring.exception.MentoringNotExpectedException;
import com.postgraduate.domain.mentoring.exception.MentoringNotWaitingException;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.entity.constant.Status;
import com.postgraduate.domain.payment.domain.service.PaymentGetService;
import com.postgraduate.domain.payment.domain.service.PaymentUpdateService;
import com.postgraduate.domain.refuse.application.dto.req.MentoringRefuseRequest;
import com.postgraduate.domain.refuse.domain.entity.Refuse;
import com.postgraduate.domain.refuse.domain.service.RefuseSaveService;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.domain.salary.domain.service.SalaryUpdateService;
import com.postgraduate.domain.senior.domain.entity.Info;
import com.postgraduate.domain.senior.domain.entity.Profile;
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

import java.time.LocalDateTime;
import java.util.Optional;

import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.*;
import static com.postgraduate.domain.senior.domain.entity.constant.Status.APPROVE;
import static com.postgraduate.domain.user.domain.entity.constant.Role.USER;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MentoringManageUseCaseTest {

    @Mock
    private CheckIsMyMentoringUseCase checkIsMyMentoringUseCase;

    @Mock
    private MentoringUpdateService mentoringUpdateService;

    @Mock
    private MentoringGetService mentoringGetService;

    @Mock
    private RefuseSaveService refuseSaveService;

    @Mock
    private AccountGetService accountGetService;

    @Mock
    private SeniorGetService seniorGetService;

    @Mock
    private SalaryGetService salaryGetService;

    @Mock
    private SalaryUpdateService salaryUpdateService;

    @Mock
    private PaymentGetService paymentGetService;

    @Mock
    private PaymentUpdateService paymentUpdateService;

    @InjectMocks
    private MentoringManageUseCase mentoringManageUseCase;

    private Long mentoringId = 1L;
    private User user;
    private Senior senior;
    private Info info;
    private Profile profile;

    @BeforeEach
    void setting() {
        info = new Info("a", "a", "a", "a", "a", "a", TRUE, TRUE, "a");
        profile = new Profile("a", "a", "a", "a", 40);
        user = new User(1L, 1234L, "a",
                "a", "123", "a",
                1, USER, TRUE, LocalDateTime.now(), LocalDateTime.now(), TRUE);
        senior = new Senior(1L, user, "a",
                APPROVE, 1, info, profile,
                LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    @DisplayName("CANCEL 상태 변경 성공 테스트")
    void updateCancel() {
        Mentoring mentoring = new Mentoring(mentoringId, user, senior
                , "a", "b", "c"
                , 40, WAITING
                , LocalDateTime.now(), LocalDateTime.now());

        Payment payment = new Payment(0L, mentoring, null, 24000, null
                , null, null, null, null, Status.DONE);

        given(checkIsMyMentoringUseCase.byUser(user, mentoringId))
                .willReturn(mentoring);

        given(paymentGetService.byMentoring(mentoring))
                .willReturn(payment);

        mentoringManageUseCase.updateCancel(user, mentoringId);

        verify(mentoringUpdateService).updateStatus(mentoring, CANCEL);
    }

    @Test
    @DisplayName("CANCEL 상태 변경 실패 테스트 - EXPECTED")
    void updateCancelFailWithExpected() {
        Mentoring mentoring = new Mentoring(mentoringId, user, senior
                , "a", "b", "c"
                , 40, EXPECTED
                , LocalDateTime.now(), LocalDateTime.now());

        given(checkIsMyMentoringUseCase.byUser(user, mentoringId))
                .willReturn(mentoring);

        assertThatThrownBy(() -> mentoringManageUseCase.updateCancel(user, mentoringId))
                .isInstanceOf(MentoringNotWaitingException.class);
    }

    @Test
    @DisplayName("CANCEL 상태 변경 실패 테스트 - DONE")
    void updateCancelFailWithDone() {
        Mentoring mentoring = new Mentoring(mentoringId, user, senior
                , "a", "b", "c"
                , 40, DONE
                , LocalDateTime.now(), LocalDateTime.now());

        given(checkIsMyMentoringUseCase.byUser(user, mentoringId))
                .willReturn(mentoring);

        assertThatThrownBy(() -> mentoringManageUseCase.updateCancel(user, mentoringId))
                .isInstanceOf(MentoringNotWaitingException.class);
    }

    @Test
    @DisplayName("DONE 상태 변경 실패 테스트 - DONE")
    void updateDoneFailWithDone() {
        Mentoring mentoring = new Mentoring(mentoringId, user, senior
                , "a", "b", "c",
                40, DONE
                , LocalDateTime.now(), LocalDateTime.now());

        given(checkIsMyMentoringUseCase.byUser(user, mentoringId))
                .willReturn(mentoring);

        assertThatThrownBy(() -> mentoringManageUseCase.updateDone(user, mentoringId))
                .isInstanceOf(MentoringNotExpectedException.class);
    }

    @Test
    @DisplayName("DONE 상태 변경 실패 테스트 - WAITING")
    void updateDoneFailWithWaiting() {
        Mentoring mentoring = new Mentoring(mentoringId, user, senior
                , "a", "b", "c"
                , 40, WAITING
                , LocalDateTime.now(), LocalDateTime.now());

        given(checkIsMyMentoringUseCase.byUser(user, mentoringId))
                .willReturn(mentoring);

        assertThatThrownBy(() -> mentoringManageUseCase.updateDone(user, mentoringId))
                .isInstanceOf(MentoringNotExpectedException.class);
    }

    @Test
    @DisplayName("DONE 상태 변경 성공 테스트")
    void updateDone() {
        Mentoring mentoring = new Mentoring(mentoringId, user, senior
                , "a", "b", "c"
                , 40, EXPECTED
                , LocalDateTime.now(), LocalDateTime.now());
        Salary salary = mock(Salary.class);
        given(checkIsMyMentoringUseCase.byUser(user, mentoringId))
                .willReturn(mentoring);
        given(salaryGetService.bySenior(mentoring.getSenior()))
                .willReturn(salary);
        mentoringManageUseCase.updateDone(user, mentoringId);

        verify(salaryUpdateService).updateTotalAmount(salary);
        verify(mentoringUpdateService).updateStatus(mentoring, DONE);
    }

    @Test
    @DisplayName("REFUSE 상태 변경 성공 테스트")
    void updateRefuse() {
        MentoringRefuseRequest request = new MentoringRefuseRequest("abc");
        Mentoring mentoring = new Mentoring(mentoringId, user, senior
                , "a", "b", "c"
                , 40, WAITING
                , LocalDateTime.now(), LocalDateTime.now());

        given(seniorGetService.byUser(user))
                .willReturn(senior);
        given(checkIsMyMentoringUseCase.bySenior(senior, mentoringId))
                .willReturn(mentoring);

        mentoringManageUseCase.updateRefuse(user, mentoringId, request);

        verify(refuseSaveService).save(any(Refuse.class));
        verify(mentoringUpdateService).updateStatus(mentoring, REFUSE);
    }

    @Test
    @DisplayName("REFUSE 상태 변경 실패 테스트 - EXPECTED")
    void updateRefuseWithExpected() {
        MentoringRefuseRequest request = new MentoringRefuseRequest("abc");
        Mentoring mentoring = new Mentoring(mentoringId, user, senior
                , "a", "b", "c"
                , 40, EXPECTED
                , LocalDateTime.now(), LocalDateTime.now());

        given(seniorGetService.byUser(user))
                .willReturn(senior);
        given(checkIsMyMentoringUseCase.bySenior(senior, mentoringId))
                .willReturn(mentoring);

        assertThatThrownBy(() -> mentoringManageUseCase.updateRefuse(user, mentoringId, request))
                .isInstanceOf(MentoringNotWaitingException.class);
    }

    @Test
    @DisplayName("REFUSE 상태 변경 실패 테스트 - DONE")
    void updateRefuseWithDone() {
        MentoringRefuseRequest request = new MentoringRefuseRequest("abc");
        Mentoring mentoring = new Mentoring(mentoringId, user, senior
                , "a", "b", "c"
                , 40, DONE
                , LocalDateTime.now(), LocalDateTime.now());

        given(seniorGetService.byUser(user))
                .willReturn(senior);
        given(checkIsMyMentoringUseCase.bySenior(senior, mentoringId))
                .willReturn(mentoring);

        assertThatThrownBy(() -> mentoringManageUseCase.updateRefuse(user, mentoringId, request))
                .isInstanceOf(MentoringNotWaitingException.class);
    }

    @Test
    @DisplayName("EXPECTED 상태 변경 성공 테스트 - 계좌 존재")
    void updateExpectedTrue() {
        MentoringDateRequest dateRequest = new MentoringDateRequest("2023-12-12");
        Mentoring mentoring = new Mentoring(mentoringId, user, senior
                , "a", "b", "c"
                , 40, WAITING
                , LocalDateTime.now(), LocalDateTime.now());

        given(seniorGetService.byUser(user)).willReturn(senior);
        given(checkIsMyMentoringUseCase.bySenior(senior, mentoringId)).willReturn(mentoring);
        given(accountGetService.bySenior(senior))
                .willReturn(Optional.of(new Account()));

        assertThat(mentoringManageUseCase.updateExpected(user, mentoringId, dateRequest))
                .isEqualTo(TRUE);

        verify(mentoringUpdateService).updateDate(mentoring, dateRequest.date());
        verify(mentoringUpdateService).updateStatus(mentoring, EXPECTED);
    }

    @Test
    @DisplayName("EXPECTED 상태 변경 성공 테스트 - 계좌 없음")
    void updateExpectedFa() {
        MentoringDateRequest dateRequest = new MentoringDateRequest("2023-12-12");
        Mentoring mentoring = new Mentoring(mentoringId, user, senior
                , "a", "b", "c"
                , 40, WAITING
                , LocalDateTime.now(), LocalDateTime.now());

        given(seniorGetService.byUser(user))
                .willReturn(senior);
        given(checkIsMyMentoringUseCase.bySenior(senior, mentoringId))
                .willReturn(mentoring);
        given(accountGetService.bySenior(senior))
                .willReturn(Optional.ofNullable(null));

        assertThat(mentoringManageUseCase.updateExpected(user, mentoringId, dateRequest))
                .isEqualTo(FALSE);

        verify(mentoringUpdateService).updateDate(mentoring, dateRequest.date());
        verify(mentoringUpdateService).updateStatus(mentoring, EXPECTED);
    }

    @Test
    @DisplayName("EXPECTED 상태 변경 실패 테스트 - EXPECTED")
    void updateExpectedFailWithExpected() {
        MentoringDateRequest dateRequest = new MentoringDateRequest("2023-12-12");
        Mentoring mentoring = new Mentoring(mentoringId, user, senior
                , "a", "b", "c"
                , 40, EXPECTED
                , LocalDateTime.now(), LocalDateTime.now());

        given(seniorGetService.byUser(user))
                .willReturn(senior);
        given(checkIsMyMentoringUseCase.bySenior(senior, mentoringId))
                .willReturn(mentoring);

        assertThatThrownBy(() -> mentoringManageUseCase.updateExpected(user, mentoringId, dateRequest))
                .isInstanceOf(MentoringNotWaitingException.class);
    }

    @Test
    @DisplayName("EXPECTED 상태 변경 실패 테스트 - DONE")
    void updateExpectedFailwithDone() {
        MentoringDateRequest dateRequest = new MentoringDateRequest("2023-12-12");
        Mentoring mentoring = new Mentoring(mentoringId, user, senior
                , "a", "b", "c"
                , 40, DONE
                , LocalDateTime.now(), LocalDateTime.now());

        given(seniorGetService.byUser(user))
                .willReturn(senior);
        given(checkIsMyMentoringUseCase.bySenior(senior, mentoringId))
                .willReturn(mentoring);

        assertThatThrownBy(() -> mentoringManageUseCase.updateExpected(user, mentoringId, dateRequest))
                .isInstanceOf(MentoringNotWaitingException.class);
    }
}
