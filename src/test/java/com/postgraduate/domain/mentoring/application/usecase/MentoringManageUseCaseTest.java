package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.account.domain.entity.Account;
import com.postgraduate.domain.account.domain.service.AccountGetService;
import com.postgraduate.domain.mentoring.application.dto.req.MentoringApplyRequest;
import com.postgraduate.domain.mentoring.application.dto.req.MentoringDateRequest;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.mentoring.domain.service.MentoringSaveService;
import com.postgraduate.domain.mentoring.domain.service.MentoringUpdateService;
import com.postgraduate.domain.mentoring.exception.MentoringNotExpectedException;
import com.postgraduate.domain.mentoring.exception.MentoringNotFoundException;
import com.postgraduate.domain.mentoring.exception.MentoringNotWaitingException;
import com.postgraduate.domain.payment.application.usecase.PaymentManageUseCase;
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

import java.time.LocalDate;
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
    private MentoringUpdateService mentoringUpdateService;

    @Mock
    private MentoringGetService mentoringGetService;

    @Mock
    private MentoringSaveService mentoringSaveService;

    @Mock
    private PaymentGetService paymentGetService;

    @Mock
    private PaymentUpdateService paymentUpdateService;

    @Mock
    private PaymentManageUseCase paymentManageUseCase;

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


    @InjectMocks
    private MentoringManageUseCase mentoringManageUseCase;

    private Long mentoringId = -1L;
    private User user;
    private Senior senior;
    private Info info;
    private Profile profile;
    private Salary salary;

    @BeforeEach
    void setting() {
        info = new Info("a", "a", "a", "a", "a", "a", TRUE, TRUE, "a");
        profile = new Profile("a", "a", "a", "a", 40);
        user = new User(-1L, 1234L, "a",
                "a", "123", "a",
                1, USER, TRUE, LocalDateTime.now(), LocalDateTime.now(), TRUE);
        senior = new Senior(-1L, user, "a",
                APPROVE, 1, info, profile,
                LocalDateTime.now(), LocalDateTime.now());
        salary = new Salary(-1L, FALSE, senior, 10000, LocalDate.now(), LocalDateTime.now(), null);
    }

    @Test
    @DisplayName("정상 실행 여부 테스트")
    void applyMentoring() {
        Payment payment = mock(Payment.class);
        User user = mock(User.class);
        MentoringApplyRequest request = new MentoringApplyRequest("1", "topic", "ques", "1201,1202,1203");

        given(paymentGetService.byUserAndOrderId(any(), any()))
                .willReturn(payment);

        mentoringManageUseCase.applyMentoringWithPayment(user, request);
        verify(mentoringSaveService).save(any());
    }

//    @ParameterizedTest
//    @ValueSource(strings = {"1201,1203", "1201", ""})
//    @DisplayName("날짜 예외 테스트 3보다 작을 경우")
//    void applyMentoringWithInvalidDatesSmaller(String dates) {
//        User user = mock(User.class);
//        Payment payment = mock(Payment.class);
//        MentoringApplyRequest request = new MentoringApplyRequest("1", "topic", "ques", dates);
//        given(paymentGetService.byUserAndOrderId(any(), any()))
//                .willReturn(payment);
//
//        assertThatThrownBy(()-> mentoringManageUseCase.applyMentoringWithPayment(user, request))
//                .isInstanceOf(MentoringDateException.class);
//    }
//
//    @ParameterizedTest
//    @ValueSource(strings = {"1201,1203,1202,1203", "1201,1202,1203,1204,1205","1201,1202,1203,1204,1205,1206"})
//    @DisplayName("날짜 예외 테스트 3보다 큰 경우")
//    void applyMentoringWithInvalidDateBigger(String dates) {
//        User user = mock(User.class);
//        Payment payment = mock(Payment.class);
//
//        MentoringApplyRequest request = new MentoringApplyRequest("1", "topic", "ques", dates);
//        given(paymentGetService.byUserAndOrderId(any(), any()))
//                .willReturn(payment);
//
//        assertThatThrownBy(()-> mentoringManageUseCase.applyMentoringWithPayment(user, request))
//                .isInstanceOf(MentoringDateException.class);
//    }
// todo: 환불 처리 관련 필요

    @Test
    @DisplayName("CANCEL 상태 변경 성공 테스트")
    void updateCancel() {
        Payment payment = new Payment(0L, user, senior, 24000, null
                , null, null, null, null, Status.DONE);
        Mentoring mentoring = new Mentoring(mentoringId, user, senior, payment, null
                , "a", "b", "c"
                , 40, WAITING
                , LocalDateTime.now(), LocalDateTime.now());

        given(mentoringGetService.byIdAndUserAndWaiting(mentoringId, user))
                .willReturn(mentoring);
        mentoringManageUseCase.updateCancel(user, mentoringId);

        verify(paymentManageUseCase)
                .refundPayByUser(user, payment.getOrderId());
        verify(mentoringUpdateService)
                .updateCancel(mentoring);
    }

    @Test
    @DisplayName("CANCEL 상태 변경 실패 테스트 - EXPECTED")
    void updateCancelFailWithExpected() {
        Payment payment = mock(Payment.class);
        Mentoring mentoring = new Mentoring(mentoringId, user, senior, payment, null
                , "a", "b", "c"
                , 40, EXPECTED
                , LocalDateTime.now(), LocalDateTime.now());

        given(mentoringGetService.byIdAndUserAndWaiting(mentoringId, user))
                .willThrow(MentoringNotFoundException.class);
        assertThatThrownBy(() -> mentoringManageUseCase.updateCancel(user, mentoringId))
                .isInstanceOf(MentoringNotFoundException.class);
    }

    @Test
    @DisplayName("CANCEL 상태 변경 실패 테스트 - DONE")
    void updateCancelFailWithDone() {
        Payment payment = mock(Payment.class);
        Mentoring mentoring = new Mentoring(mentoringId, user, senior, payment, null
                , "a", "b", "c"
                , 40, DONE
                , LocalDateTime.now(), LocalDateTime.now());
        given(mentoringGetService.byIdAndUserAndWaiting(mentoringId, user))
                .willThrow(MentoringNotFoundException.class);
        assertThatThrownBy(() -> mentoringManageUseCase.updateCancel(user, mentoringId))
                .isInstanceOf(MentoringNotFoundException.class);
    }

    @Test
    @DisplayName("DONE 상태 변경 실패 테스트 - DONE")
    void updateDoneFailWithDone() {
        Payment payment = mock(Payment.class);
        Mentoring mentoring = new Mentoring(mentoringId, user, senior, payment, null
                , "a", "b", "c",
                40, DONE
                , LocalDateTime.now(), LocalDateTime.now());
        given(mentoringGetService.byIdAndUserAndExpected(mentoringId, user))
                .willThrow(MentoringNotFoundException.class);
        assertThatThrownBy(() -> mentoringManageUseCase.updateDone(user, mentoringId))
                .isInstanceOf(MentoringNotFoundException.class);
    }

    @Test
    @DisplayName("DONE 상태 변경 실패 테스트 - WAITING")
    void updateDoneFailWithWaiting() {
        Payment payment = mock(Payment.class);
        Mentoring mentoring = new Mentoring(mentoringId, user, senior, payment, null
                , "a", "b", "c"
                , 40, WAITING
                , LocalDateTime.now(), LocalDateTime.now());
        given(mentoringGetService.byIdAndUserAndExpected(mentoringId, user))
                .willThrow(MentoringNotFoundException.class);
        assertThatThrownBy(() -> mentoringManageUseCase.updateDone(user, mentoringId))
                .isInstanceOf(MentoringNotFoundException.class);
    }

//    @Test
//    @DisplayName("DONE 상태 변경 성공 테스트")
//    void updateDone() {
//        Payment payment = mock(Payment.class);
//        Mentoring mentoring = new Mentoring(mentoringId, user, senior, payment, null
//                , "a", "b", "c"
//                , 40, EXPECTED
//                , LocalDateTime.now(), LocalDateTime.now());
//
//        given(mentoringGetService.byIdAndUserAndExpected(mentoringId, user))
//                .willReturn(mentoring);
//        given(salaryGetService.bySenior(mentoring.getSenior()))
//                .willReturn(salary);
//
//        verify(mentoringUpdateService)
//                .updateDone(mentoring, salary);
//    }

//    @Test
//    @DisplayName("REFUSE 상태 변경 성공 테스트")
//    void updateRefuse() {
//        MentoringRefuseRequest request = new MentoringRefuseRequest("abc");
//        Payment payment = mock(Payment.class);
//        Mentoring mentoring = new Mentoring(mentoringId, user, senior, payment, null
//                , "a", "b", "c"
//                , 40, WAITING
//                , LocalDateTime.now(), LocalDateTime.now());
//
//        given(mentoringGetService.byIdAndUserAndWaiting(mentoringId, user))
//                .willThrow(MentoringNotFoundException.class);
//        assertThatThrownBy(() -> mentoringManageUseCase.updateDone(user, mentoringId))
//                .isInstanceOf(MentoringNotFoundException.class);
//    }

    @Test
    @DisplayName("REFUSE 상태 변경 실패 테스트 - EXPECTED")
    void updateRefuseWithExpected() {
        MentoringRefuseRequest request = new MentoringRefuseRequest("abc");
        Payment payment = mock(Payment.class);
        Mentoring mentoring = new Mentoring(mentoringId, user, senior, payment, null
                , "a", "b", "c"
                , 40, EXPECTED
                , LocalDateTime.now(), LocalDateTime.now());
        given(seniorGetService.byUser(user))
                .willReturn(senior);
        given(mentoringGetService.byIdAndSeniorAndWaiting(mentoringId, senior))
                .willThrow(MentoringNotFoundException.class);
        assertThatThrownBy(() -> mentoringManageUseCase.updateRefuse(user, mentoringId, request))
                .isInstanceOf(MentoringNotFoundException.class);
    }

    @Test
    @DisplayName("REFUSE 상태 변경 실패 테스트 - DONE")
    void updateRefuseWithDone() {
        MentoringRefuseRequest request = new MentoringRefuseRequest("abc");
        Payment payment = mock(Payment.class);
        Mentoring mentoring = new Mentoring(mentoringId, user, senior, payment, null
                , "a", "b", "c"
                , 40, DONE
                , LocalDateTime.now(), LocalDateTime.now());

        given(seniorGetService.byUser(user))
                .willReturn(senior);
        given(mentoringGetService.byIdAndSeniorAndWaiting(mentoringId, senior))
                .willThrow(MentoringNotFoundException.class);
        assertThatThrownBy(() -> mentoringManageUseCase.updateRefuse(user, mentoringId, request))
                .isInstanceOf(MentoringNotFoundException.class);
    }

//    @Test
//    @DisplayName("EXPECTED 상태 변경 성공 테스트 - 계좌 존재")
//    void updateExpectedTrue() {
//        MentoringDateRequest dateRequest = new MentoringDateRequest("2023-12-12");
//        Payment payment = mock(Payment.class);
//        Mentoring mentoring = new Mentoring(mentoringId, user, senior, payment, null
//                , "a", "b", "c"
//                , 40, WAITING
//                , LocalDateTime.now(), LocalDateTime.now());
//
//        given(mentoringGetService.byMentoringId(any()))
//                .willReturn(mentoring);
//        given(seniorGetService.byUser(user))
//                .willReturn(senior);
//        given(accountGetService.bySenior(senior))
//                .willReturn(Optional.of(new Account()));
//
//        assertThat(mentoringManageUseCase.updateExpected(user, mentoringId, dateRequest))
//                .isEqualTo(TRUE);
//
//        verify(mentoringUpdateService).updateExpected(mentoring, dateRequest.date());
//    }

//    @Test
//    @DisplayName("EXPECTED 상태 변경 성공 테스트 - 계좌 없음")
//    void updateExpectedFa() {
//        MentoringDateRequest dateRequest = new MentoringDateRequest("2023-12-12");
//        Payment payment = mock(Payment.class);
//        Mentoring mentoring = new Mentoring(mentoringId, user, senior, payment, null
//                , "a", "b", "c"
//                , 40, WAITING
//                , LocalDateTime.now(), LocalDateTime.now());
//
//        given(mentoringGetService.byMentoringId(any()))
//                .willReturn(mentoring);
//        given(seniorGetService.byUser(any()))
//                .willReturn(senior);
//        given(accountGetService.bySenior(any()))
//                .willReturn(Optional.ofNullable(null));
//
//        assertThat(mentoringManageUseCase.updateExpected(user, mentoringId, dateRequest))
//                .isEqualTo(FALSE);
//
//        verify(mentoringUpdateService).updateExpected(mentoring, dateRequest.date());
//    }

    @Test
    @DisplayName("EXPECTED 상태 변경 실패 테스트 - EXPECTED")
    void updateExpectedFailWithExpected() {
        MentoringDateRequest dateRequest = new MentoringDateRequest("2023-12-12");
        Payment payment = mock(Payment.class);
        Mentoring mentoring = new Mentoring(mentoringId, user, senior, payment, null
                , "a", "b", "c"
                , 40, EXPECTED
                , LocalDateTime.now(), LocalDateTime.now());

        given(seniorGetService.byUser(user))
                .willReturn(senior);
        given(mentoringGetService.byIdAndSeniorAndWaiting(mentoringId, senior))
                .willThrow(MentoringNotFoundException.class);
        assertThatThrownBy(() -> mentoringManageUseCase.updateExpected(user, mentoringId, dateRequest))
                .isInstanceOf(MentoringNotFoundException.class);
    }

    @Test
    @DisplayName("EXPECTED 상태 변경 실패 테스트 - DONE")
    void updateExpectedFailwithDone() {
        MentoringDateRequest dateRequest = new MentoringDateRequest("2023-12-12");
        Payment payment = mock(Payment.class);
        Mentoring mentoring = new Mentoring(mentoringId, user, senior, payment, null
                , "a", "b", "c"
                , 40, DONE
                , LocalDateTime.now(), LocalDateTime.now());

        given(seniorGetService.byUser(user))
                .willReturn(senior);
        given(mentoringGetService.byIdAndSeniorAndWaiting(mentoringId, senior))
                .willThrow(MentoringNotFoundException.class);
        assertThatThrownBy(() -> mentoringManageUseCase.updateExpected(user, mentoringId, dateRequest))
                .isInstanceOf(MentoringNotFoundException.class);
    }
}
