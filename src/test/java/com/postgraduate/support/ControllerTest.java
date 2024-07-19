package com.postgraduate.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postgraduate.domain.auth.application.usecase.jwt.JwtUseCase;
import com.postgraduate.domain.auth.application.usecase.oauth.SelectOauth;
import com.postgraduate.domain.auth.application.usecase.oauth.SignUpUseCase;
import com.postgraduate.domain.auth.application.usecase.oauth.kakao.KakaoSignInUseCase;
import com.postgraduate.domain.auth.presentation.AuthController;
import com.postgraduate.domain.mentoring.application.usecase.MentoringManageUseCase;
import com.postgraduate.domain.mentoring.application.usecase.MentoringSeniorInfoUseCase;
import com.postgraduate.domain.mentoring.application.usecase.MentoringUserInfoUseCase;
import com.postgraduate.domain.mentoring.presentation.MentoringController;
import com.postgraduate.domain.salary.application.usecase.SalaryInfoUseCase;
import com.postgraduate.domain.salary.presentation.SalaryController;
import com.postgraduate.domain.senior.application.usecase.SeniorInfoUseCase;
import com.postgraduate.domain.senior.application.usecase.SeniorManageUseCase;
import com.postgraduate.domain.senior.application.usecase.SeniorMyPageUseCase;
import com.postgraduate.domain.senior.presentation.SeniorController;
import com.postgraduate.domain.user.quit.application.usecase.QuitManageUseCase;
import com.postgraduate.domain.user.user.application.usecase.UserManageUseCase;
import com.postgraduate.domain.user.user.application.usecase.UserMyPageUseCase;
import com.postgraduate.domain.user.user.presentation.UserController;
import com.postgraduate.global.aop.lock.DistributeLockAspect;
import com.postgraduate.global.slack.SlackLogErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(controllers = {
        UserController.class,
        MentoringController.class,
        SeniorController.class,
        SalaryController.class,
        AuthController.class
})
@AutoConfigureMockMvc
public class ControllerTest {

    @Autowired
    protected MockMvc mvc;
    @Autowired
    protected WebApplicationContext ctx;
    @Autowired
    protected ObjectMapper objectMapper;
    @MockBean
    protected MentoringUserInfoUseCase mentoringUserInfoUseCase;
    @MockBean
    protected MentoringSeniorInfoUseCase mentoringSeniorInfoUseCase;
    @MockBean
    protected MentoringManageUseCase mentoringManageUseCase;
    @MockBean
    protected UserManageUseCase userManageUseCase;
    @MockBean
    protected UserMyPageUseCase userMyPageUseCase;
    @MockBean
    protected SeniorMyPageUseCase seniorMyPageUseCase;
    @MockBean
    protected SeniorManageUseCase seniorManageUseCase;
    @MockBean
    protected SeniorInfoUseCase seniorInfoUseCase;
    @MockBean
    protected SalaryInfoUseCase salaryInfoUseCase;
    @MockBean
    protected KakaoSignInUseCase kakaoSignInUseCase;
    @MockBean
    protected SelectOauth selectOauth;
    @MockBean
    protected SignUpUseCase signUpUseCase;
    @MockBean
    protected JwtUseCase jwtUseCase;
    @MockBean
    protected QuitManageUseCase quitManageUseCase;
    @MockBean
    protected SlackLogErrorMessage slackLogErrorMessage;
    @MockBean
    protected DistributeLockAspect distributeLockAspect;
    protected Resource resource = new Resource();

}
