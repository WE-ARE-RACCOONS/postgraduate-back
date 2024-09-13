package com.postgraduate.domain.mentoring.presentation;

import com.postgraduate.domain.mentoring.application.dto.*;
import com.postgraduate.domain.mentoring.application.dto.req.MentoringApplyRequest;
import com.postgraduate.domain.mentoring.application.dto.req.MentoringDateRequest;
import com.postgraduate.domain.mentoring.application.dto.res.*;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.application.dto.req.MentoringRefuseRequest;
import com.postgraduate.domain.member.senior.domain.entity.Info;
import com.postgraduate.domain.member.senior.domain.entity.Profile;
import com.postgraduate.domain.member.senior.domain.entity.Senior;
import com.postgraduate.domain.member.user.domain.entity.User;
import com.postgraduate.support.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.util.List;

import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseCode.*;
import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseMessage.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MentoringControllerTest extends ControllerTest {
    private static final String BEARER = "Bearer token";
    Mentoring waitingMentoring = resource.getWaitingMentoring();
    Mentoring expectedMentoring = resource.getExpectedMentoring();
    Mentoring doneMentoring = resource.getDoneMentoring();
    Senior senior = resource.getSenior();
    Info info = senior.getInfo();
    Profile profile = senior.getProfile();
    User userOfSenior = resource.getSeniorUser();
    User user = resource.getUser();

    @Test
    @DisplayName("대학생이 확정대기 상태의 멘토링 목록을 조회한다")
    @WithMockUser
    void getWaitingMentorings() throws Exception {
        WaitingMentoringInfo mentoringInfo = new WaitingMentoringInfo(waitingMentoring.getMentoringId(), senior.getSeniorId(), userOfSenior.getProfile(), userOfSenior.getNickName(), info.getPostgradu(), info.getMajor(), info.getLab(), waitingMentoring.getTerm());
        WaitingMentoringResponse waitingResponse = new WaitingMentoringResponse(List.of(mentoringInfo));

        given(mentoringUserInfoUseCase.getWaiting(any()))
                .willReturn(waitingResponse);

        mvc.perform(get("/mentoring/me/waiting")
                        .header(HttpHeaders.AUTHORIZATION, BEARER ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(MENTORING_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_MENTORING_LIST_INFO.getMessage()))
                .andExpect(jsonPath("$.data.mentoringInfos[0].mentoringId").value(mentoringInfo.mentoringId()))
                .andExpect(jsonPath("$.data.mentoringInfos[0].seniorId").value(mentoringInfo.seniorId()))
                .andExpect(jsonPath("$.data.mentoringInfos[0].profile").value(mentoringInfo.profile()))
                .andExpect(jsonPath("$.data.mentoringInfos[0].nickName").value(mentoringInfo.nickName()))
                .andExpect(jsonPath("$.data.mentoringInfos[0].postgradu").value(mentoringInfo.postgradu()))
                .andExpect(jsonPath("$.data.mentoringInfos[0].major").value(mentoringInfo.major()))
                .andExpect(jsonPath("$.data.mentoringInfos[0].lab").value(mentoringInfo.lab()))
                .andExpect(jsonPath("$.data.mentoringInfos[0].term").value(mentoringInfo.term()));

    }

    @Test
    @WithMockUser
    @DisplayName("대학생이 예정된 멘토링 목록을 조회한다")
    void getExpectedMentorings() throws Exception {
        ExpectedMentoringInfo mentoringInfo = new ExpectedMentoringInfo(expectedMentoring.getMentoringId(), senior.getSeniorId(), userOfSenior.getProfile(), userOfSenior.getNickName(), info.getPostgradu(), info.getMajor(), info.getLab(), expectedMentoring.getDate(), expectedMentoring.getTerm(), info.getChatLink());
        ExpectedMentoringResponse expectedResponse = new ExpectedMentoringResponse(List.of(mentoringInfo));
        given(mentoringUserInfoUseCase.getExpected(any()))
                .willReturn(expectedResponse);

        mvc.perform(get("/mentoring/me/expected")
                        .header(HttpHeaders.AUTHORIZATION, BEARER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(MENTORING_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_MENTORING_LIST_INFO.getMessage()))
                .andExpect(jsonPath("$.data.mentoringInfos[0].mentoringId").value(mentoringInfo.mentoringId()))
                .andExpect(jsonPath("$.data.mentoringInfos[0].seniorId").value(mentoringInfo.seniorId()))
                .andExpect(jsonPath("$.data.mentoringInfos[0].profile").value(mentoringInfo.profile()))
                .andExpect(jsonPath("$.data.mentoringInfos[0].nickName").value(mentoringInfo.nickName()))
                .andExpect(jsonPath("$.data.mentoringInfos[0].postgradu").value(mentoringInfo.postgradu()))
                .andExpect(jsonPath("$.data.mentoringInfos[0].major").value(mentoringInfo.major()))
                .andExpect(jsonPath("$.data.mentoringInfos[0].lab").value(mentoringInfo.lab()))
                .andExpect(jsonPath("$.data.mentoringInfos[0].date").value(mentoringInfo.date()))
                .andExpect(jsonPath("$.data.mentoringInfos[0].term").value(mentoringInfo.term()))
                .andExpect(jsonPath("$.data.mentoringInfos[0].chatLink").value(mentoringInfo.chatLink()));
    }

    @Test
    @WithMockUser
    @DisplayName("대학생이 완료된 멘토링 목록을 조회한다.")
    void getDoneMentorings() throws Exception {
        DoneMentoringInfo mentoringInfo = new DoneMentoringInfo(expectedMentoring.getMentoringId(), senior.getSeniorId(), userOfSenior.getProfile(), userOfSenior.getNickName(), info.getPostgradu(), info.getMajor(), info.getLab(), expectedMentoring.getDate(), expectedMentoring.getTerm());
        DoneMentoringResponse doneResponse = new DoneMentoringResponse(List.of(mentoringInfo));
        given(mentoringUserInfoUseCase.getDone(any()))
                .willReturn(doneResponse);

        mvc.perform(get("/mentoring/me/done")
                        .header(HttpHeaders.AUTHORIZATION, BEARER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(MENTORING_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_MENTORING_LIST_INFO.getMessage()))
                .andExpect(jsonPath("$.data.mentoringInfos[0].mentoringId").value(mentoringInfo.mentoringId()))
                .andExpect(jsonPath("$.data.mentoringInfos[0].seniorId").value(mentoringInfo.seniorId()))
                .andExpect(jsonPath("$.data.mentoringInfos[0].profile").value(mentoringInfo.profile()))
                .andExpect(jsonPath("$.data.mentoringInfos[0].nickName").value(mentoringInfo.nickName()))
                .andExpect(jsonPath("$.data.mentoringInfos[0].postgradu").value(mentoringInfo.postgradu()))
                .andExpect(jsonPath("$.data.mentoringInfos[0].major").value(mentoringInfo.major()))
                .andExpect(jsonPath("$.data.mentoringInfos[0].lab").value(mentoringInfo.lab()))
                .andExpect(jsonPath("$.data.mentoringInfos[0].date").value(mentoringInfo.date()))
                .andExpect(jsonPath("$.data.mentoringInfos[0].term").value(mentoringInfo.term()));
    }

    @Test
    @WithMockUser
    @DisplayName("대학생이 신청한 멘토링 상세 조회한다.")
    void getMentoringDetail() throws Exception {
        AppliedMentoringDetailResponse detailResponse = new AppliedMentoringDetailResponse(senior.getSeniorId(), userOfSenior.getProfile(), userOfSenior.getNickName(), info.getPostgradu(), info.getMajor(), info.getLab(), waitingMentoring.getTopic(), waitingMentoring.getQuestion(), waitingMentoring.getDate().split(","));
        given(mentoringUserInfoUseCase.getMentoringDetail(any(), any()))
                .willReturn(detailResponse);

        mvc.perform(get("/mentoring/me/{mentoringId}", waitingMentoring.getMentoringId())
                        .header(HttpHeaders.AUTHORIZATION, BEARER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(MENTORING_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_MENTORING_DETAIL_INFO.getMessage()))
                .andExpect(jsonPath("$.data.seniorId").value(detailResponse.seniorId()))
                .andExpect(jsonPath("$.data.profile").value(detailResponse.profile()))
                .andExpect(jsonPath("$.data.nickName").value(detailResponse.nickName()))
                .andExpect(jsonPath("$.data.postgradu").value(detailResponse.postgradu()))
                .andExpect(jsonPath("$.data.major").value(detailResponse.major()))
                .andExpect(jsonPath("$.data.lab").value(detailResponse.lab()))
                .andExpect(jsonPath("$.data.topic").value(detailResponse.topic()))
                .andExpect(jsonPath("$.data.question").value(detailResponse.question()));
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @WithMockUser
    @DisplayName("대학생이 멘토링을 신청한다.")
    void applyMentoring(boolean tf) throws Exception {
        String request = objectMapper.writeValueAsString(new MentoringApplyRequest("orderId", "topic", "question", "date1,date2,date3"));

        ApplyingResponse applyingResponse = new ApplyingResponse(tf);
        given(mentoringManageUseCase.applyMentoring(any(), any()))
                .willReturn(applyingResponse);

        mvc.perform(post("/mentoring/applying")
                        .header(HttpHeaders.AUTHORIZATION, BEARER )
                        .content(request)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(MENTORING_CREATE.getCode()))
                .andExpect(jsonPath("$.message").value(CREATE_MENTORING.getMessage()))
                .andExpect(jsonPath("$.data.account").value(applyingResponse.account()));
    }

    @Test
    @WithMockUser
    @DisplayName("대학생이 멘토링을 완료한다.")
    void updateMentoringDone() throws Exception {
        willDoNothing()
                .given(mentoringManageUseCase)
                .updateDone(any(), any());

        mvc.perform(patch("/mentoring/me/{mentoringId}/done", expectedMentoring.getMentoringId())
                        .with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, BEARER ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(MENTORING_UPDATE.getCode()))
                .andExpect(jsonPath("$.message").value(UPDATE_MENTORING.getMessage()));
    }

    @Test
    @WithMockUser
    @DisplayName("대학생이 멘토링을 취소한다.")
    void updateMentoringCancel() throws Exception {
        willDoNothing()
                .given(mentoringManageUseCase)
                .updateCancel(any(), any());

        mvc.perform(patch("/mentoring/me/{mentoringId}/cancel", waitingMentoring.getMentoringId())
                        .with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, BEARER ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(MENTORING_UPDATE.getCode()))
                .andExpect(jsonPath("$.message").value(UPDATE_MENTORING.getMessage()));
    }

    @Test
    @WithMockUser("SENIOR")
    @DisplayName("대학원생이 확정 대기 멘토링 목록을 조회한다.")
    void getSeniorWaitingMentorings() throws Exception {
        WaitingSeniorMentoringInfo waitingMentoringInfo = new WaitingSeniorMentoringInfo(waitingMentoring.getMentoringId(), user.getProfile(), user.getNickName(), waitingMentoring.getTerm(), "remain");
        WaitingSeniorMentoringResponse mentoringResponse = new WaitingSeniorMentoringResponse(List.of(waitingMentoringInfo));
        given(mentoringSeniorInfoUseCase.getSeniorWaiting(any()))
                .willReturn(mentoringResponse);

        mvc.perform(get("/mentoring/senior/me/waiting")
                        .header(HttpHeaders.AUTHORIZATION, BEARER ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(MENTORING_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_MENTORING_LIST_INFO.getMessage()))
                .andExpect(jsonPath("$.data.seniorMentoringInfos[0].mentoringId").value(waitingMentoringInfo.mentoringId()))
                .andExpect(jsonPath("$.data.seniorMentoringInfos[0].profile").value(waitingMentoringInfo.profile()))
                .andExpect(jsonPath("$.data.seniorMentoringInfos[0].nickName").value(waitingMentoringInfo.nickName()))
                .andExpect(jsonPath("$.data.seniorMentoringInfos[0].term").value(waitingMentoringInfo.term()))
                .andExpect(jsonPath("$.data.seniorMentoringInfos[0].remainTime").value(waitingMentoringInfo.remainTime()));
    }

    @Test
    @WithMockUser("SENIOR")
    @DisplayName("대학원생이 진행 예정 멘토링 목록을 조회한다.")
    void getSeniorExpectedMentorings() throws Exception {
        ExpectedSeniorMentoringInfo expectedMentoringInfo = new ExpectedSeniorMentoringInfo(expectedMentoring.getMentoringId(), user.getProfile(), user.getNickName(), expectedMentoring.getTerm(), expectedMentoring.getDate(), 1L);
        ExpectedSeniorMentoringResponse mentoringResponse = new ExpectedSeniorMentoringResponse(List.of(expectedMentoringInfo));
        given(mentoringSeniorInfoUseCase.getSeniorExpected(any()))
                .willReturn(mentoringResponse);

        mvc.perform(get("/mentoring/senior/me/expected")
                        .header(HttpHeaders.AUTHORIZATION, BEARER ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(MENTORING_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_MENTORING_LIST_INFO.getMessage()))
                .andExpect(jsonPath("$.data.seniorMentoringInfos[0].mentoringId").value(expectedMentoringInfo.mentoringId()))
                .andExpect(jsonPath("$.data.seniorMentoringInfos[0].profile").value(expectedMentoringInfo.profile()))
                .andExpect(jsonPath("$.data.seniorMentoringInfos[0].nickName").value(expectedMentoringInfo.nickName()))
                .andExpect(jsonPath("$.data.seniorMentoringInfos[0].term").value(expectedMentoringInfo.term()))
                .andExpect(jsonPath("$.data.seniorMentoringInfos[0].date").value(expectedMentoringInfo.date()))
                .andExpect(jsonPath("$.data.seniorMentoringInfos[0].dDay").value(expectedMentoringInfo.dDay()));
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @WithMockUser("SENIOR")
    @DisplayName("대학원생이 완료 멘토링 목록을 조회한다.")
    void getSeniorDoneMentorings(boolean tf) throws Exception {
        DoneSeniorMentoringInfo doneMentoringInfo = new DoneSeniorMentoringInfo(doneMentoring.getMentoringId(), user.getProfile(), user.getNickName(), doneMentoring.getTerm(), doneMentoring.getDate(), LocalDate.now(), tf);
        DoneSeniorMentoringResponse mentoringResponse = new DoneSeniorMentoringResponse(List.of(doneMentoringInfo));
        given(mentoringSeniorInfoUseCase.getSeniorDone(any()))
                .willReturn(mentoringResponse);

        mvc.perform(get("/mentoring/senior/me/done")
                        .header(HttpHeaders.AUTHORIZATION, BEARER ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(MENTORING_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_MENTORING_LIST_INFO.getMessage()))
                .andExpect(jsonPath("$.data.seniorMentoringInfos[0].mentoringId").value(doneMentoringInfo.mentoringId()))
                .andExpect(jsonPath("$.data.seniorMentoringInfos[0].profile").value(doneMentoringInfo.profile()))
                .andExpect(jsonPath("$.data.seniorMentoringInfos[0].nickName").value(doneMentoringInfo.nickName()))
                .andExpect(jsonPath("$.data.seniorMentoringInfos[0].term").value(doneMentoringInfo.term()))
                .andExpect(jsonPath("$.data.seniorMentoringInfos[0].date").value(doneMentoringInfo.date()))
                .andExpect(jsonPath("$.data.seniorMentoringInfos[0].salaryDate").value(doneMentoringInfo.salaryDate().toString()))
                .andExpect(jsonPath("$.data.seniorMentoringInfos[0].status").value(doneMentoringInfo.status()));
    }

    @Test
    @WithMockUser("SENIOR")
    @DisplayName("대학원생이 멘토링을 상세조회합니다.")
    void getSeniorMentoringDetails() throws Exception {
        SeniorMentoringDetailResponse detailResponse = new SeniorMentoringDetailResponse(user.getProfile(), user.getNickName(), waitingMentoring.getTopic(), waitingMentoring.getQuestion(), waitingMentoring.getDate().split(","), waitingMentoring.getTerm());
        given(mentoringSeniorInfoUseCase.getSeniorMentoringDetail(any(), any()))
                .willReturn(detailResponse);

        mvc.perform(get("/mentoring/senior/me/{mentoringId}", waitingMentoring.getMentoringId())
                        .header(HttpHeaders.AUTHORIZATION, BEARER ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(MENTORING_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_MENTORING_DETAIL_INFO.getMessage()))
                .andExpect(jsonPath("$.data.profile").value(detailResponse.profile()))
                .andExpect(jsonPath("$.data.nickName").value(detailResponse.nickName()))
                .andExpect(jsonPath("$.data.topic").value(detailResponse.topic()))
                .andExpect(jsonPath("$.data.question").value(detailResponse.question()))
                .andExpect(jsonPath("$.data.dates").isArray())
                .andExpect(jsonPath("$.data.term").value(detailResponse.term()));
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @WithMockUser("SENIOR")
    @DisplayName("대학원생이 멘토링을 수락한다.")
    void updateSeniorMentoringExpected(boolean tf) throws Exception {
        String request = objectMapper.writeValueAsString(new MentoringDateRequest("date"));

        given(mentoringManageUseCase.updateExpected(any(), any(), any()))
                .willReturn(tf);

        mvc.perform(patch("/mentoring/senior/me/{mentoringId}/expected", waitingMentoring.getMentoringId())
                        .header(HttpHeaders.AUTHORIZATION, BEARER )
                        .with(csrf())
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(MENTORING_UPDATE.getCode()))
                .andExpect(jsonPath("$.message").value(UPDATE_MENTORING.getMessage()))
                .andExpect(jsonPath("$.data").value(tf));
    }

    @Test
    @WithMockUser("SENIOR")
    @DisplayName("대학원생이 멘토링을 거절한다.")
    void updateSeniorMentoringRefuse() throws Exception {
        String request = objectMapper.writeValueAsString(new MentoringRefuseRequest("reason"));

        willDoNothing()
                .given(mentoringManageUseCase)
                .updateRefuse(any(), any(), any());

        mvc.perform(patch("/mentoring/senior/me/{mentoringId}/refuse", waitingMentoring.getMentoringId())
                        .header(HttpHeaders.AUTHORIZATION, BEARER )
                        .with(csrf())
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(MENTORING_UPDATE.getCode()))
                .andExpect(jsonPath("$.message").value(UPDATE_MENTORING.getMessage()));
    }
}