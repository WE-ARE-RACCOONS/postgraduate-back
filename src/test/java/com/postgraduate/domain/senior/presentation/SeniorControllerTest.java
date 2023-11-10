package com.postgraduate.domain.senior.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postgraduate.domain.senior.application.dto.req.SeniorCertificationRequest;
import com.postgraduate.domain.senior.application.dto.req.SeniorProfileAndAccountRequest;
import com.postgraduate.domain.senior.application.dto.req.SeniorProfileRequest;
import com.postgraduate.domain.senior.application.dto.req.SeniorSignUpRequest;
import com.postgraduate.domain.senior.application.dto.res.SeniorInfoResponse;
import com.postgraduate.domain.senior.application.dto.res.SeniorProfileResponse;
import com.postgraduate.domain.senior.application.mapper.SeniorMapper;
import com.postgraduate.domain.senior.application.usecase.SeniorMyPageUseCase;
import com.postgraduate.domain.senior.application.usecase.SeniorSignUpUseCase;
import com.postgraduate.domain.senior.domain.entity.Account;
import com.postgraduate.domain.senior.domain.entity.Info;
import com.postgraduate.domain.senior.domain.entity.Profile;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.entity.constant.Status;
import com.postgraduate.domain.user.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDate;

import static com.postgraduate.domain.senior.presentation.constant.SeniorResponseCode.*;
import static com.postgraduate.domain.senior.presentation.constant.SeniorResponseMessage.*;
import static com.postgraduate.domain.user.domain.entity.constant.Role.USER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SeniorController.class)
class SeniorControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @MockBean
    private SeniorSignUpUseCase seniorSignUpUseCase;
    @MockBean
    private SeniorMyPageUseCase seniorMyPageUseCase;
    private User testUser;
    private Senior testSenior;

    @BeforeEach
    public void setUp(final WebApplicationContext context) throws Exception {
        testUser = User.builder()
                .userId(1000000000L)
                .socialId(1000000000L)
                .email("test.com")
                .role(USER)
                .nickName("test")
                .profile("test")
                .point(0)
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .build();
        Account account = new Account("account", "bank", "123");
        Profile profile = new Profile("info", "you", "abc", "1000", 10);
        Info info = new Info("c", "m", "p", "p", "a", "f");
        testSenior = Senior.builder()
                .seniorId(100000000L)
                .certification("certification")
                .hit(0)
                .account(account)
                .profile(profile)
                .info(info)
                .status(Status.APPROVE)
                .user(testUser)
                .build();

        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .alwaysDo(MockMvcResultHandlers.print()) // andDo(print()) 코드 포함
                .addFilters(new CharacterEncodingFilter("UTF-8", true)) // 한글 깨짐 방지
                .build();
    }

    @Test
    @DisplayName("대학원생 등록")
    void signUpSenior() throws Exception {
        //given
        SeniorSignUpRequest seniorSignUpRequest = new SeniorSignUpRequest(
                "test","test","test","test","test","test","test","test","test", "test");
        String body = objectMapper.writeValueAsString(seniorSignUpRequest);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post("/senior/signup")
                .with(SecurityMockMvcRequestPostProcessors.user(testUser.getUserId().toString()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header("Authorization", "Bearer Token"));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_CREATE.getCode()))
                .andExpect(jsonPath("$.message").value(CREATE_SENIOR.getMessage()));
    }

    @Test
    @DisplayName("대학원생 프로필 등록")
    void signUpSeniorWithProfile() throws Exception {
        //given
        SeniorProfileRequest seniorProfileRequest = new SeniorProfileRequest("test", "test", "test", "test");
        String body = objectMapper.writeValueAsString(seniorProfileRequest);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .patch("/senior/profile")
                .with(SecurityMockMvcRequestPostProcessors.user(testUser.getUserId().toString()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header("Authorization", "Bearer Token"));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_UPDATE.getCode()))
                .andExpect(jsonPath("$.message").value(UPDATE_PROFILE.getMessage()));
    }

    @Test
    @DisplayName("대학원생 인증")
    void updateCertification() throws Exception {
        //given
        SeniorCertificationRequest seniorCertificationRequest = new SeniorCertificationRequest("test");
        String body = objectMapper.writeValueAsString(seniorCertificationRequest);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .patch("/senior/certification")
                .with(SecurityMockMvcRequestPostProcessors.user(testUser.getUserId().toString()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header("Authorization", "Bearer Token"));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_UPDATE.getCode()))
                .andExpect(jsonPath("$.message").value(UPDATE_CERTIFICATION.getMessage()));
    }

    @Test
    @DisplayName("대학원생 인증")
    void updateCertificationFail() throws Exception {
        //given
        SeniorCertificationRequest seniorCertificationRequest = new SeniorCertificationRequest("test");
        String body = objectMapper.writeValueAsString(seniorCertificationRequest);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .patch("/senior/certification")
                .with(SecurityMockMvcRequestPostProcessors.user(testUser.getUserId().toString()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header("Authorization", "Bearer Token"));
        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_UPDATE.getCode()))
                .andExpect(jsonPath("$.message").value(UPDATE_CERTIFICATION.getMessage()));
    }

    @Test
    @DisplayName("대학원생 기본 정보")
    void getInfo() throws Exception {
        //given
        SeniorInfoResponse seniorInfoResponse = SeniorMapper.mapToSeniorInfo(testSenior, Status.APPROVE, true);
        given(seniorMyPageUseCase.seniorInfo(any()))
                .willReturn(seniorInfoResponse);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/senior/me")
                .with(SecurityMockMvcRequestPostProcessors.user(testUser.getUserId().toString()))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer Token"));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_SENIOR_INFO.getMessage()))
                .andExpect(jsonPath("$.data.nickName").value(seniorInfoResponse.getNickName()))
                .andExpect(jsonPath("$.data.profile").value(seniorInfoResponse.getProfile()))
                .andExpect(jsonPath("$.data.certificationRegister").value(seniorInfoResponse.getCertificationRegister().toString()))
                .andExpect(jsonPath("$.data.profileRegister").value(true));

    }

    @Test
    @DisplayName("대학원생 프로필 정보")
    void getProfile() throws Exception {
        //given
        Profile profile = new Profile("test", "test", "test", "test", 10);
        SeniorProfileResponse seniorProfileResponse = new SeniorProfileResponse(profile, "ac", "ba");
        given(seniorMyPageUseCase.getSeniorProfile(any()))
                .willReturn(seniorProfileResponse);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/senior/me/profile")
                .with(SecurityMockMvcRequestPostProcessors.user(testUser.getUserId().toString()))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer Token"));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_SENIOR_PROFILE.getMessage()))
                .andExpect(jsonPath("$.data.profile.info").value(seniorProfileResponse.getProfile().getInfo()))
                .andExpect(jsonPath("$.data.profile.target").value(seniorProfileResponse.getProfile().getTarget()))
                .andExpect(jsonPath("$.data.profile.chatLink").value(seniorProfileResponse.getProfile().getChatLink()))
                .andExpect(jsonPath("$.data.profile.time").value(seniorProfileResponse.getProfile().getTime()))
                .andExpect(jsonPath("$.data.profile.term").value(seniorProfileResponse.getProfile().getTerm()))
                .andExpect(jsonPath("$.data.account").value(seniorProfileResponse.getAccount()))
                .andExpect(jsonPath("$.data.bank").value(seniorProfileResponse.getBank()));
    }

    @Test
    @DisplayName("대학원생 프로필 수정")
    void updateProfile() throws Exception {
        //given
        SeniorProfileAndAccountRequest request = new SeniorProfileAndAccountRequest(
                "test","test","test","test","test","test","test"
        );
        String body = objectMapper.writeValueAsString(request);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .patch("/senior/me/profile")
                .with(SecurityMockMvcRequestPostProcessors.user(testUser.getUserId().toString()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header("Authorization", "Bearer Token"));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_UPDATE.getCode()))
                .andExpect(jsonPath("$.message").value(UPDATE_PROFILE.getMessage()));
    }
}