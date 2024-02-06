package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.mentoring.application.dto.req.MentoringApplyRequest;
import com.postgraduate.domain.mentoring.application.mapper.MentoringMapper;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringSaveService;
import com.postgraduate.domain.mentoring.exception.MentoringDateException;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.service.PaymentGetService;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.senior.domain.entity.Info;
import com.postgraduate.domain.senior.domain.entity.Profile;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.entity.constant.Status;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.user.domain.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static com.postgraduate.domain.mentoring.application.mapper.MentoringMapper.mapToMentoring;
import static com.postgraduate.domain.user.domain.entity.constant.Role.SENIOR;
import static com.postgraduate.domain.user.domain.entity.constant.Role.USER;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MentoringApplyUseCaseTest {


    @Mock
    private MentoringSaveService mentoringSaveService;

    @Mock
    private PaymentGetService paymentGetService;

    @InjectMocks
    private MentoringApplyUseCase mentoringApplyUseCase;

    @Test
    @DisplayName("정상 실행 여부 테스트")
    void applyMentoring() {
        Payment payment = mock(Payment.class);
        User user = mock(User.class);
        Senior senior = mock(Senior.class);
        Salary salary = mock(Salary.class);
        MentoringApplyRequest request = new MentoringApplyRequest("1", "topic", "ques", "1201,1202,1203");

        given(paymentGetService.byOrderId(any()))
                .willReturn(payment);
        given(payment.getSalary())
                .willReturn(salary);
        given(salary.getSenior())
                .willReturn(senior);

        mentoringApplyUseCase.applyMentoringWithPayment(user, request);
        verify(mentoringSaveService).save(any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1201,1203", "1201", ""})
    @DisplayName("날짜 예외 테스트 3보다 작을 경우")
    void applyMentoringWithInvalidDatesSmaller(String dates) {
        User user = mock(User.class);
        Payment payment = mock(Payment.class);
        MentoringApplyRequest request = new MentoringApplyRequest("1", "topic", "ques", dates);
        given(paymentGetService.byOrderId(any()))
                .willReturn(payment);

        assertThatThrownBy(()-> mentoringApplyUseCase.applyMentoringWithPayment(user, request))
                .isInstanceOf(MentoringDateException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1201,1203,1202,1203", "1201,1202,1203,1204,1205","1201,1202,1203,1204,1205,1206"})
    @DisplayName("날짜 예외 테스트 3보다 큰 경우")
    void applyMentoringWithInvalidDateBigger(String dates) {
        User user = mock(User.class);
        Payment payment = mock(Payment.class);

        MentoringApplyRequest request = new MentoringApplyRequest("1", "topic", "ques", dates);
        given(paymentGetService.byOrderId(any()))
                .willReturn(payment);

        assertThatThrownBy(()-> mentoringApplyUseCase.applyMentoringWithPayment(user, request))
                .isInstanceOf(MentoringDateException.class);
    }
}
