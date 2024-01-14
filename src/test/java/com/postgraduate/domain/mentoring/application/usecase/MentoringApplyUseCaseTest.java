package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.mentoring.application.dto.req.MentoringApplyRequest;
import com.postgraduate.domain.mentoring.application.mapper.MentoringMapper;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringSaveService;
import com.postgraduate.domain.mentoring.exception.MentoringDateException;
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
    private SeniorGetService seniorGetService;

    @Mock
    private MentoringSaveService mentoringSaveService;

    @InjectMocks
    private MentoringApplyUseCase mentoringApplyUseCase;

    @Test
    @DisplayName("정상 실행 여부 테스트")
    void applyMentoring() {
        User user = new User(-1L, -1234L, "abc.com", "abc"
                , " 123123", "abcab", 0
                , USER, TRUE, LocalDateTime.now(), LocalDateTime.now(), FALSE);
        User seniorUser = new User(-11L, -12345L, "abc.com", "qwe"
                , " 123123", "abcab", 0
                , USER, TRUE, LocalDateTime.now(), LocalDateTime.now(), FALSE);
        Senior senior = new Senior(1L, seniorUser, "a", Status.WAITING,
                100, new Info(), new Profile(), now(), now());
        MentoringApplyRequest request = new MentoringApplyRequest(senior.getSeniorId(), "topic", "ques", "1201,1202,1203");

        given(seniorGetService.bySeniorId(request.seniorId()))
                .willReturn(senior);
        Mentoring mentoring = mapToMentoring(user, senior, request);
        given(mentoringSaveService.save(any()))
                .willReturn(mentoring);

        assertThat(mentoringApplyUseCase.applyMentoring(user, request))
                .isEqualTo(mentoring.getMentoringId());
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
