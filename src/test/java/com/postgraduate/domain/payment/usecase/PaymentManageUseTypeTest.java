package com.postgraduate.domain.payment.usecase;

import com.postgraduate.domain.payment.application.dto.req.PaymentResultRequest;
import com.postgraduate.domain.payment.application.usecase.PaymentManageUseCase;
import com.postgraduate.domain.payment.domain.service.PaymentGetService;
import com.postgraduate.domain.payment.domain.service.PaymentSaveService;
import com.postgraduate.domain.payment.domain.service.PaymentUpdateService;
import com.postgraduate.domain.member.senior.domain.entity.Info;
import com.postgraduate.domain.member.senior.domain.entity.Profile;
import com.postgraduate.domain.member.senior.domain.entity.Senior;
import com.postgraduate.domain.member.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.member.user.domain.entity.User;
import com.postgraduate.domain.member.user.domain.service.UserGetService;
import com.postgraduate.global.slack.SlackPaymentMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;

import static com.postgraduate.domain.member.senior.domain.entity.constant.Status.APPROVE;
import static com.postgraduate.domain.member.user.domain.entity.constant.Role.SENIOR;
import static com.postgraduate.domain.member.user.domain.entity.constant.Role.USER;
import static java.lang.Boolean.TRUE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PaymentManageUseTypeTest {
    @Mock
    private PaymentSaveService paymentSaveService;
    @Mock
    private PaymentGetService paymentGetService;
    @Mock
    private PaymentUpdateService paymentUpdateService;
    @Mock
    private SeniorGetService seniorGetService;
    @Mock
    private UserGetService userGetService;
    @Mock
    private WebClient webClient;
    @Mock
    private SlackPaymentMessage slackPaymentMessage;
    @InjectMocks
    private PaymentManageUseCase paymentManageUseCase;

    private User user;
    private Senior senior;
    private Info info;
    private Profile profile;
    private User mentoringUser;

    @BeforeEach
    void setting() {
        info = new Info("a", "a", "a", "a", "a", "a", TRUE, TRUE, "a", "chatLink", 30);
        profile = new Profile("a", "a", "a");
        user = new User(-1L, 1234L, "a",
                "a", "123", "a",
                0, SENIOR, TRUE, LocalDateTime.now(), LocalDateTime.now(), TRUE, TRUE);
        mentoringUser = new User(-2L, 12345L, "a",
                "a", "123", "a",
                0, USER, TRUE, LocalDateTime.now(), LocalDateTime.now(), TRUE, TRUE);
        senior = new Senior(-1L, user, "a",
                APPROVE, 1,1, info, profile,
                LocalDateTime.now(), LocalDateTime.now(), null, null);
    }

    @Test
    @DisplayName("결제 저장 테스트")
    void savePay() {
        PaymentResultRequest paymentResultRequest = new PaymentResultRequest("success", "0000", "0000", "success" ,"123", "123", "123","123", "a", "a","a", "20240202020202");

        given(userGetService.byUserId(any()))
                .willReturn(mentoringUser);
        given(seniorGetService.bySeniorNickName(any()))
                .willReturn(senior);
        paymentManageUseCase.savePay(paymentResultRequest);

        verify(paymentSaveService)
                .save(any());
    }

    @Test
    @DisplayName("결제 취소 테스트")
    void savePayFail() {
        PaymentResultRequest paymentResultRequest = new PaymentResultRequest("fail", "0000", "0000", "success" ,"123", "123", "123","123", "a", "a","a", "20240202020202");
        paymentManageUseCase.savePay(paymentResultRequest);

        verify(paymentSaveService, times(0))
                .save(any());
    }
    //todo : webclient 관련 테스트 필요
}
