package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.mentoring.application.dto.req.MentoringApplyRequest;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringSaveService;
import com.postgraduate.domain.mentoring.exception.MentoringDateException;
import com.postgraduate.domain.senior.domain.entity.Senior;
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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MentoringApplyUseCaseTest {

    @Mock
    private SeniorGetService seniorGetService;

    @Mock
    private MentoringSaveService mentoringSaveService;

    @InjectMocks
    private MentoringApplyUseCase mentoringApplyUseCase;

    @Test
    @DisplayName("정상 실행 여부 테스트")
    void applyMentoring() {
        User user = mock(User.class);
        MentoringApplyRequest request = new MentoringApplyRequest(-1L, "topic", "ques", "1201,1202,1203");

        Senior senior = mock(Senior.class);
        given(seniorGetService.bySeniorId(request.seniorId()))
                .willReturn(senior);
        mentoringApplyUseCase.applyMentoring(user, request);

        verify(mentoringSaveService).save(any(Mentoring.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1201,1203", "1201", ""})
    @DisplayName("날짜 예외 테스트 3보다 작을 경우")
    void applyMentoringWithInvalidDatesSmaller(String dates) {
        User user = mock(User.class);
        MentoringApplyRequest request = new MentoringApplyRequest(-1L, "topic", "ques", dates);

        assertThatThrownBy(()-> mentoringApplyUseCase.applyMentoring(user, request))
                .isInstanceOf(MentoringDateException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1201,1203,1202,1203", "1201,1202,1203,1204,1205","1201,1202,1203,1204,1205,1206"})
    @DisplayName("날짜 예외 테스트 3보다 큰 경우")
    void applyMentoringWithInvalidDateBigger(String dates) {
        User user = mock(User.class);
        MentoringApplyRequest request = new MentoringApplyRequest(-1L, "topic", "ques", dates);

        assertThatThrownBy(()-> mentoringApplyUseCase.applyMentoring(user, request))
                .isInstanceOf(MentoringDateException.class);
    }
}
