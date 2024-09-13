package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.member.senior.domain.entity.Account;
import com.postgraduate.domain.member.user.domain.entity.Wish;
import com.postgraduate.domain.mentoring.application.dto.req.MentoringApplyRequest;
import com.postgraduate.domain.mentoring.application.dto.res.ApplyingResponse;
import com.postgraduate.domain.mentoring.application.mapper.MentoringMapper;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.mentoring.domain.service.MentoringSaveService;
import com.postgraduate.domain.mentoring.exception.MentoringDateException;
import com.postgraduate.domain.mentoring.exception.MentoringPresentException;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.entity.constant.Status;
import com.postgraduate.domain.payment.domain.service.PaymentGetService;
import com.postgraduate.domain.payment.exception.PaymentNotFoundException;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.member.senior.domain.entity.Info;
import com.postgraduate.domain.member.senior.domain.entity.Profile;
import com.postgraduate.domain.member.senior.domain.entity.Senior;
import com.postgraduate.domain.member.senior.domain.service.SeniorUpdateService;
import com.postgraduate.domain.member.user.domain.entity.User;
import com.postgraduate.global.bizppurio.application.usecase.BizppurioJuniorMessage;
import com.postgraduate.global.bizppurio.application.usecase.BizppurioSeniorMessage;
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

import static com.postgraduate.domain.member.senior.domain.entity.constant.Status.APPROVE;
import static com.postgraduate.domain.member.user.domain.entity.constant.Role.SENIOR;
import static com.postgraduate.domain.member.user.domain.entity.constant.Role.USER;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MentoringApplyingUseTypeTest {
    @Mock
    private PaymentGetService paymentGetService;
    @Mock
    private MentoringGetService mentoringGetService;
    @Mock
    private MentoringSaveService mentoringSaveService;
    @Mock
    private MentoringMapper mentoringMapper;
    @Mock
    private SeniorUpdateService seniorUpdateService;
    @Mock
    private BizppurioSeniorMessage bizppurioSeniorMessage;
    @Mock
    private BizppurioJuniorMessage bizppurioJuniorMessage;
    @InjectMocks
    private MentoringApplyingUseCase mentoringApplyingUseCase;

    private User user;
    private Senior senior;
    private Info info;
    private Profile profile;
    private Salary salary;
    private Account account;
    private Payment payment;
    private User mentoringUser;

    @BeforeEach
    void setting() {
        info = new Info("a", "a", "a", "a", "a", "a", TRUE, TRUE, "a", "chatLink", 30);
        profile = new Profile("a", "a", "a");
        user = new User(-1L, 1234L, "a",
                "a", "123", "a",
                0, SENIOR, TRUE, LocalDateTime.now(), LocalDateTime.now(), TRUE, TRUE, new Wish());
        mentoringUser = new User(-2L, 12345L, "a",
                "a", "123", "a",
                0, USER, TRUE, LocalDateTime.now(), LocalDateTime.now(), TRUE, TRUE, null);
        senior = new Senior(-1L, user, "a",
                APPROVE,1, 1, info, profile,
                LocalDateTime.now(), LocalDateTime.now(), null, null);
        salary = new Salary(-1L, FALSE, senior, 10000, LocalDate.now(), LocalDateTime.now(), null);
        account = new Account(-1L, "1", "은행", "유저", senior);
        payment = new Payment(-1L, mentoringUser, senior, 20000, "a", "a", "a", LocalDateTime.now(), null, Status.DONE);
    }
//    @Test
//    @DisplayName("멘토링 신청 성공 테스트 - account존재")
//    void applyMentoringWithAccount() {
//        MentoringApplyRequest request = new MentoringApplyRequest("00", "abc", "abc", "1213,1231,123");
//        given(paymentGetService.byUserAndOrderId(mentoringUser, request.orderId()))
//                .willReturn(payment);
//        given(senior.getAccount())
//                .willReturn(new Account());
//
//        ApplyingResponse applyingResponse = mentoringApplyingUseCase.applyMentoringWithPayment(mentoringUser, request);
//
//        verify(mentoringSaveService)
//                .saveMentoring(any());
//        assertThat(applyingResponse.account())
//                .isEqualTo(TRUE);
//    }

    @Test
    @DisplayName("멘토링 신청 성공 테스트 - account존재x")
    void applyMentoringWithOutAccount() {
        MentoringApplyRequest request = new MentoringApplyRequest("00", "abc", "abc", "1213,1231,123");
        given(paymentGetService.byUserAndOrderId(mentoringUser, request.orderId()))
                .willReturn(payment);

        ApplyingResponse applyingResponse = mentoringApplyingUseCase.applyMentoringWithPayment(mentoringUser, request);

        verify(mentoringSaveService)
                .saveMentoring(any());
        assertThat(applyingResponse.account())
                .isEqualTo(FALSE);
    }

    @Test
    @DisplayName("멘토링 신청 실패 테스트 PaymentNotFoundException")
    void applyMentoringFailPaymentNotFoundException() {
        MentoringApplyRequest request = new MentoringApplyRequest("00", "abc", "abc", "1213,1231,123");
        given(paymentGetService.byUserAndOrderId(mentoringUser, request.orderId()))
                .willThrow(PaymentNotFoundException.class);

        assertThatThrownBy(() -> mentoringApplyingUseCase.applyMentoringWithPayment(mentoringUser, request))
                .isInstanceOf(PaymentNotFoundException.class);
    }

    @Test
    @DisplayName("멘토링 신청 실패 테스트 MentoringPresentException")
    void applyMentoringFailMentoringPresentException() {
        MentoringApplyRequest request = new MentoringApplyRequest("00", "abc", "abc", "1213,1231,123");
        given(paymentGetService.byUserAndOrderId(mentoringUser, request.orderId()))
                .willReturn(payment);
        willThrow(MentoringPresentException.class)
                .given(mentoringGetService)
                        .checkByPayment(payment);

        assertThatThrownBy(() -> mentoringApplyingUseCase.applyMentoringWithPayment(mentoringUser, request))
                .isInstanceOf(MentoringPresentException.class);
    }

    @Test
    @DisplayName("멘토링 신청 실패 테스트 MentoringDateException")
    void applyMentoringFailMentoringDateException() {
        MentoringApplyRequest request = new MentoringApplyRequest("00", "abc", "abc", "1213");
        given(paymentGetService.byUserAndOrderId(mentoringUser, request.orderId()))
                .willReturn(payment);

        assertThatThrownBy(() -> mentoringApplyingUseCase.applyMentoringWithPayment(mentoringUser, request))
                .isInstanceOf(MentoringDateException.class);
    }
}
