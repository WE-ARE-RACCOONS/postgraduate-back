package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.account.domain.entity.Account;
import com.postgraduate.domain.account.domain.service.AccountGetService;
import com.postgraduate.domain.mentoring.application.dto.req.MentoringApplyRequest;
import com.postgraduate.domain.mentoring.application.dto.req.MentoringDateRequest;
import com.postgraduate.domain.mentoring.application.dto.res.ApplyingResponse;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.mentoring.domain.service.MentoringUpdateService;
import com.postgraduate.domain.mentoring.exception.MentoringDateException;
import com.postgraduate.domain.mentoring.exception.MentoringNotFoundException;
import com.postgraduate.domain.mentoring.exception.MentoringPresentException;
import com.postgraduate.domain.payment.application.usecase.PaymentManageUseCase;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.entity.constant.Status;
import com.postgraduate.domain.payment.exception.PaymentNotFoundException;
import com.postgraduate.domain.refuse.application.dto.req.MentoringRefuseRequest;
import com.postgraduate.domain.refuse.domain.service.RefuseSaveService;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.domain.salary.domain.service.SalaryUpdateService;
import com.postgraduate.domain.senior.domain.entity.Info;
import com.postgraduate.domain.senior.domain.entity.Profile;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.global.bizppurio.usecase.BizppurioJuniorMessage;
import com.postgraduate.global.bizppurio.usecase.BizppurioSeniorMessage;
import com.postgraduate.global.slack.SlackErrorMessage;
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

import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.WAITING;
import static com.postgraduate.domain.senior.domain.entity.constant.Status.APPROVE;
import static com.postgraduate.domain.user.domain.entity.constant.Role.SENIOR;
import static com.postgraduate.domain.user.domain.entity.constant.Role.USER;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MentoringManageUseTypeTest {
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
    private PaymentManageUseCase paymentManageUseCase;
    @Mock
    private SlackErrorMessage slackErrorMessage;
    @Mock
    private BizppurioSeniorMessage bizppurioSeniorMessage;
    @Mock
    private BizppurioJuniorMessage bizppurioJuniorMessage;
    @Mock
    private MentoringApplyingUseCase mentoringApplyingUseCase;
    @InjectMocks
    private MentoringManageUseCase mentoringManageUseCase;

    private Long mentoringId = -1L;
    private User user;
    private Senior senior;
    private Info info;
    private Profile profile;
    private Salary salary;
    private Account account;
    private Payment payment;
    private User mentoringUser;
    private Mentoring mentoring;

    @BeforeEach
    void setting() {
        info = new Info("a", "a", "a", "a", "a", "a", TRUE, TRUE, "a", "chatLink", 30);
        profile = new Profile("a", "a", "a");
        user = new User(-1L, 1234L, "a",
                "a", "123", "a",
                0, SENIOR, TRUE, LocalDateTime.now(), LocalDateTime.now(), TRUE);
        mentoringUser = new User(-2L, 12345L, "a",
                "a", "123", "a",
                0, USER, TRUE, LocalDateTime.now(), LocalDateTime.now(), TRUE);
        senior = new Senior(-1L, user, "a",
                APPROVE, 1, info, profile,
                LocalDateTime.now(), LocalDateTime.now());
        salary = new Salary(-1L, FALSE, senior, 10000, LocalDate.now(), LocalDateTime.now(), null);
        account = new Account(-1L, "1", "은행", "유저", senior);
        payment = new Payment(-1L, mentoringUser, senior, 20000, "a", "a", "a", LocalDateTime.now(), null, Status.DONE);
        mentoring = new Mentoring(-1L, mentoringUser, senior, payment, salary, "asd", "asd", "1201,1202,1203", 30, WAITING, LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    @DisplayName("정상 실행 여부 테스트 - with Account")
    void applyMentoringWithAccount() {
        MentoringApplyRequest request = new MentoringApplyRequest("-1", "topic", "ques", "1201,1202,1203");
        ApplyingResponse applyingResponse = new ApplyingResponse(true);

        given(mentoringApplyingUseCase.applyMentoringWithPayment(mentoringUser, request))
                .willReturn(applyingResponse);

        ApplyingResponse response = mentoringManageUseCase.applyMentoring(mentoringUser, request);
        assertThat(response.account())
                .isEqualTo(TRUE);
    }

    @Test
    @DisplayName("정상 실행 여부 테스트 - withOut Account")
    void applyMentoringWithOutAccount() {
        MentoringApplyRequest request = new MentoringApplyRequest("-1", "topic", "ques", "1201,1202,1203");
        ApplyingResponse applyingResponse = new ApplyingResponse(false);

        given(mentoringApplyingUseCase.applyMentoringWithPayment(mentoringUser, request))
                .willReturn(applyingResponse);

        ApplyingResponse response = mentoringManageUseCase.applyMentoring(mentoringUser, request);
        assertThat(response.account())
                .isEqualTo(FALSE);
    }

    @Test
    @DisplayName("결제건을 찾을 수 없는 경우")
    void PaymentNotFoundException() {
        MentoringApplyRequest request = new MentoringApplyRequest("-1", "topic", "ques", "1201,1202,1203");

        given(mentoringApplyingUseCase.applyMentoringWithPayment(mentoringUser, request))
                .willThrow(PaymentNotFoundException.class);

        assertThatThrownBy(() -> mentoringManageUseCase.applyMentoring(mentoringUser, request))
                .isInstanceOf(PaymentNotFoundException.class);
    }

    @Test
    @DisplayName("이미 신청된 결제건인 경우")
    void MentoringPresentException() {
        MentoringApplyRequest request = new MentoringApplyRequest("-1", "topic", "question", "1201,1202,1203");

        given(mentoringApplyingUseCase.applyMentoringWithPayment(mentoringUser, request))
                .willThrow(MentoringPresentException.class);

        assertThatThrownBy(() -> mentoringManageUseCase.applyMentoring(mentoringUser, request))
                .isInstanceOf(MentoringPresentException.class);
    }

    @Test
    @DisplayName("이외의 예외 환불 테스트")
    void ExceptionWithRefund() {
        MentoringApplyRequest request = new MentoringApplyRequest("-1", "topic", "question", "1201,1202");

        given(mentoringApplyingUseCase.applyMentoringWithPayment(mentoringUser, request))
                .willThrow(MentoringDateException.class);

        assertThatThrownBy(() -> mentoringManageUseCase.applyMentoring(mentoringUser, request))
                .isInstanceOf(MentoringDateException.class);
        verify(paymentManageUseCase)
                .refundPayByUser(mentoringUser, request.orderId());
    }

    @Test
    @DisplayName("CANCEL 상태 변경 성공 테스트")
    void updateCancel() {
        given(mentoringGetService.byIdAndUserAndWaiting(mentoringId, mentoringUser))
                .willReturn(mentoring);
        mentoringManageUseCase.updateCancel(mentoringUser, mentoringId);

        verify(paymentManageUseCase)
                .refundPayByUser(mentoringUser, payment.getOrderId());
        verify(mentoringUpdateService)
                .updateCancel(mentoring);
    }

    @Test
    @DisplayName("CANCEL 상태 변경 실패 테스트")
    void updateCancelFail() {
       given(mentoringGetService.byIdAndUserAndWaiting(mentoringId, mentoringUser))
                .willThrow(MentoringNotFoundException.class);
        assertThatThrownBy(() -> mentoringManageUseCase.updateCancel(mentoringUser, mentoringId))
                .isInstanceOf(MentoringNotFoundException.class);
    }

    @Test
    @DisplayName("DONE 상태 변경 성공 테스트")
    void updateDone() {
        given(mentoringGetService.byIdAndUserAndExpected(mentoringId, mentoringUser))
                .willReturn(mentoring);
        given(salaryGetService.bySenior(mentoring.getSenior()))
                .willReturn(salary);
        mentoringManageUseCase.updateDone(mentoringUser, mentoringId);

        verify(mentoringUpdateService)
                .updateDone(mentoring, salary);
        verify(salaryUpdateService)
                .plusTotalAmount(salary, mentoring.calculateForSenior());
    }

    @Test
    @DisplayName("DONE 상태 변경 실패 테스트")
    void updateDoneFail() {
        given(mentoringGetService.byIdAndUserAndExpected(mentoringId, user))
                .willThrow(MentoringNotFoundException.class);
        assertThatThrownBy(() -> mentoringManageUseCase.updateDone(user, mentoringId))
                .isInstanceOf(MentoringNotFoundException.class);
    }

    @Test
    @DisplayName("REFUSE 상태 변경 성공 테스트")
    void updateRefuse() {
        MentoringRefuseRequest request = new MentoringRefuseRequest("reason");
        given(seniorGetService.byUser(user))
                .willReturn(senior);
        given(mentoringGetService.byIdAndSeniorAndWaiting(mentoringId, senior))
                .willReturn(mentoring);
        mentoringManageUseCase.updateRefuse(user, mentoringId, request);

        verify(refuseSaveService)
                .save(any());
        verify(paymentManageUseCase)
                .refundPayBySenior(senior, payment.getOrderId());
        verify(mentoringUpdateService)
                .updateRefuse(mentoring);
    }

    @Test
    @DisplayName("REFUSE 상태 변경 실패 테스트")
    void updateRefuseFail() {
        MentoringRefuseRequest request = new MentoringRefuseRequest("reason");
        given(seniorGetService.byUser(user))
                .willReturn(senior);
        given(mentoringGetService.byIdAndSeniorAndWaiting(mentoringId, senior))
                .willThrow(MentoringNotFoundException.class);
        assertThatThrownBy(() -> mentoringManageUseCase.updateRefuse(user, mentoringId, request))
                .isInstanceOf(MentoringNotFoundException.class);
    }

    @Test
    @DisplayName("EXPECTED 상태 변경 성공 테스트 - 계좌 존재")
    void updateExpectedTrue() {
        MentoringDateRequest dateRequest = new MentoringDateRequest("2023-12-12-18-00");

        given(seniorGetService.byUser(user))
                .willReturn(senior);
        given(mentoringGetService.byIdAndSeniorAndWaiting(mentoringId, senior))
                .willReturn(mentoring);
        given(accountGetService.bySenior(senior))
                .willReturn(Optional.of(account));

        assertThat(mentoringManageUseCase.updateExpected(user, mentoringId, dateRequest))
                .isEqualTo(TRUE);
        verify(mentoringUpdateService)
                .updateExpected(mentoring, dateRequest.date());
    }

    @Test
    @DisplayName("EXPECTED 상태 변경 성공 테스트 - 계좌 없음")
    void updateExpectedWithOoutACcount() {
        MentoringDateRequest dateRequest = new MentoringDateRequest("2023-12-12-18-00");

        given(seniorGetService.byUser(user))
                .willReturn(senior);
        given(mentoringGetService.byIdAndSeniorAndWaiting(mentoringId, senior))
                .willReturn(mentoring);
        given(accountGetService.bySenior(senior))
                .willReturn(Optional.ofNullable(null));

        assertThat(mentoringManageUseCase.updateExpected(user, mentoringId, dateRequest))
                .isEqualTo(FALSE);
        verify(mentoringUpdateService)
                .updateExpected(mentoring, dateRequest.date());
    }

    @Test
    @DisplayName("EXPECTED 상태 변경 실패 테스트")
    void updateExpectedFail() {
        MentoringDateRequest dateRequest = new MentoringDateRequest("2023-12-12");

        given(seniorGetService.byUser(user))
                .willReturn(senior);
        given(mentoringGetService.byIdAndSeniorAndWaiting(mentoringId, senior))
                .willThrow(MentoringNotFoundException.class);
        assertThatThrownBy(() -> mentoringManageUseCase.updateExpected(user, mentoringId, dateRequest))
                .isInstanceOf(MentoringNotFoundException.class);
    }
}
