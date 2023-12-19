package com.postgraduate.domain.mentoring.application.usecase;

import com.postgraduate.domain.mentoring.application.dto.req.MentoringApplyRequest;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringSaveService;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.user.domain.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    @DisplayName("실행 여부 테스트")
    void applyMentoring() {
        User user = mock(User.class);
        MentoringApplyRequest request = new MentoringApplyRequest(-1L, "topic", "ques", "1201");

        Senior senior = mock(Senior.class);
        given(seniorGetService.bySeniorId(request.seniorId()))
                .willReturn(senior);
        mentoringApplyUseCase.applyMentoring(user, request);

        verify(mentoringSaveService).save(any(Mentoring.class));
    }
}
