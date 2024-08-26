package com.postgraduate.domain.senior.presentation;

import com.postgraduate.domain.senior.available.application.dto.req.AvailableCreateRequest;
import com.postgraduate.domain.senior.available.application.dto.res.AvailableTimeResponse;
import com.postgraduate.domain.senior.available.application.dto.res.AvailableTimesResponse;
import com.postgraduate.domain.senior.available.domain.entity.Available;
import com.postgraduate.domain.senior.application.dto.req.*;
import com.postgraduate.domain.senior.application.dto.res.*;
import com.postgraduate.domain.senior.domain.entity.Info;
import com.postgraduate.domain.senior.domain.entity.Profile;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.user.domain.entity.User;
import com.postgraduate.support.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static com.postgraduate.domain.senior.presentation.constant.SeniorResponseCode.*;
import static com.postgraduate.domain.senior.presentation.constant.SeniorResponseMessage.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SeniorControllerTest extends ControllerTest {
    private static final String BEARER = "Bearer token";

    Senior senior = resource.getSenior();
    User userOfSenior = resource.getSeniorUser();
    User user = resource.getUser();
    List<Available> availables = resource.getAvailables();

    @Test
    @WithMockUser
    @DisplayName("모든 SeniorId조회")
    void getAllSeniorId() throws Exception {
        AllSeniorIdResponse response = new AllSeniorIdResponse(List.of(1l,2l,3l,4l,5l));
        given(seniorInfoUseCase.getAllSeniorId())
                        .willReturn(response);

        mvc.perform(get("/senior/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_SENIOR_ID_LIST.getMessage()))
                .andExpect(jsonPath("$.data.seniorIds").isArray());
    }

    @Test
    @WithMockUser("SENIOR")
    @DisplayName("대학원생 인증한다")
    void updateCertification() throws Exception {
        String request = objectMapper.writeValueAsString(
                new SeniorCertificationRequest("certification")
        );

        willDoNothing().given(seniorManageUseCase)
                        .updateCertification(any(), any());

        mvc.perform(patch("/senior/certification")
                        .header(HttpHeaders.AUTHORIZATION, BEARER)
                        .with(csrf())
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_UPDATE.getCode()))
                .andExpect(jsonPath("$.message").value(UPDATE_CERTIFICATION.getMessage()));
    }

    @Test
    @WithMockUser("SENIOR")
    @DisplayName("대학원생 프로필을 등록한다")
    void singUpSenior() throws Exception {
        List<AvailableCreateRequest> availableCreateRequests = List.of(
                new AvailableCreateRequest("월", "17:00", "23:00"),
                new AvailableCreateRequest("금", "10:00", "20:00"),
                new AvailableCreateRequest("토", "10:00", "20:00")
        );
        String request = objectMapper.writeValueAsString(
                new SeniorProfileRequest("저는요", "대상", "한줄소개", availableCreateRequests)
        );
        SeniorProfileUpdateResponse response = new SeniorProfileUpdateResponse(senior.getSeniorId());

        given(seniorManageUseCase.signUpProfile(any(), any()))
                .willReturn(response);

        mvc.perform(patch("/senior/profile")
                        .header(HttpHeaders.AUTHORIZATION, BEARER)
                        .with(csrf())
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_UPDATE.getCode()))
                .andExpect(jsonPath("$.message").value(UPDATE_PROFILE.getMessage()))
                .andExpect(jsonPath("$.data.seniorId").value(response.seniorId()));
    }

    @Test
    @WithMockUser("SENIOR")
    @DisplayName("대학원생 정산 계좌를 생성한다")
    void updateAccount() throws Exception {
        String request = objectMapper.writeValueAsString(
                new SeniorAccountRequest("농협", "주인", "123123123456")
        );

        willDoNothing().given(seniorManageUseCase)
                .saveAccount(any(), any());

        mvc.perform(post("/senior/account")
                        .header(HttpHeaders.AUTHORIZATION, BEARER)
                        .with(csrf())
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_CREATE.getCode()))
                .andExpect(jsonPath("$.message").value(CREATE_ACCOUNT.getMessage()));
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @WithMockUser("SENIOR")
    @DisplayName("대학원생 마이페이지 기본 정보를 조회한다")
    void getSeniorInfo(boolean tf) throws Exception {
        SeniorMyPageResponse response = new SeniorMyPageResponse(userOfSenior.getSocialId(), senior.getSeniorId(), userOfSenior.getNickName(), userOfSenior.getProfile(), senior.getStatus(), tf);
        given(seniorMyPageUseCase.getSeniorMyPage(any()))
                .willReturn(response);

        mvc.perform(get("/senior/me")
                        .header(HttpHeaders.AUTHORIZATION, BEARER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_SENIOR_INFO.getMessage()))
                .andExpect(jsonPath("$.data.socialId").value(response.socialId()))
                .andExpect(jsonPath("$.data.seniorId").value(response.seniorId()))
                .andExpect(jsonPath("$.data.nickName").value(response.nickName()))
                .andExpect(jsonPath("$.data.profile").value(response.profile()))
                .andExpect(jsonPath("$.data.certificationRegister").value(response.certificationRegister().toString()))
                .andExpect(jsonPath("$.data.profileRegister").value(tf));
    }

    @Test
    @WithMockUser("SENIOR")
    @DisplayName("대학원생 마이페이지 프로필 수정시 기존 정보를 조회한다")
    void getSeniorProfile() throws Exception {
        Info info = senior.getInfo();
        Profile profile = senior.getProfile();
        List<AvailableTimeResponse> availableTimeResponses = availables.stream()
                .map(available -> new AvailableTimeResponse(available.getDay(), available.getStartTime(), available.getEndTime()))
                .toList();
        SeniorMyPageProfileResponse response = new SeniorMyPageProfileResponse(info.getLab(), info.getKeyword().split(","), profile.getInfo(), profile.getTarget(), info.getChatLink(), info.getField().split(","), profile.getOneLiner(), availableTimeResponses);

        given(seniorMyPageUseCase.getSeniorMyPageProfile(any()))
                .willReturn(response);

        mvc.perform(get("/senior/me/profile")
                        .header(HttpHeaders.AUTHORIZATION, BEARER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_SENIOR_MYPAGE_PROFILE.getMessage()))
                .andExpect(jsonPath("$.data.lab").value(response.lab()))
                .andExpect(jsonPath("$.data.keyword").isArray())
                .andExpect(jsonPath("$.data.info").value(response.info()))
                .andExpect(jsonPath("$.data.target").value(response.target()))
                .andExpect(jsonPath("$.data.chatLink").value(response.chatLink()))
                .andExpect(jsonPath("$.data.field").isArray())
                .andExpect(jsonPath("$.data.oneLiner").value(response.oneLiner()))
                .andExpect(jsonPath("$.data.times").isArray());
    }

    @Test
    @WithMockUser("SENIOR")
    @DisplayName("대학원생 마이페이지 프로필을 수정한다")
    void updateSeniorProfile() throws Exception {
        List<AvailableCreateRequest> availableCreateRequests = List.of(
                new AvailableCreateRequest("월", "17:00", "23:00"),
                new AvailableCreateRequest("금", "10:00", "20:00"),
                new AvailableCreateRequest("토", "10:00", "20:00")
        );
        String request = objectMapper.writeValueAsString(
                new SeniorMyPageProfileRequest("lab", "keyword1,keyword2", "info", "target", "chatLink", "AI", "oneliner", availableCreateRequests)
        );


        SeniorProfileUpdateResponse response = new SeniorProfileUpdateResponse(senior.getSeniorId());

        given(seniorManageUseCase.updateSeniorMyPageProfile(any(), any()))
                .willReturn(response);

        mvc.perform(patch("/senior/me/profile")
                        .header(HttpHeaders.AUTHORIZATION, BEARER)
                        .with(csrf())
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_UPDATE.getCode()))
                .andExpect(jsonPath("$.message").value(UPDATE_MYPAGE_PROFILE.getMessage()))
                .andExpect(jsonPath("$.data.seniorId").value(response.seniorId()));
    }

    @Test
    @WithMockUser("SENIOR")
    @DisplayName("대학원생 마이페이지 계정 설정시 기존 정보를 조회한다")
    void getSeniorUserAccountWithNull() throws Exception {
        SeniorMyPageUserAccountResponse response = new SeniorMyPageUserAccountResponse(userOfSenior.getProfile(), userOfSenior.getPhoneNumber(), userOfSenior.getNickName());
        given(seniorMyPageUseCase.getSeniorMyPageUserAccount(any()))
                .willReturn(response);

        mvc.perform(get("/senior/me/account")
                        .header(HttpHeaders.AUTHORIZATION, BEARER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_SENIOR_MYPAGE_ACCOUNT.getMessage()))
                .andExpect(jsonPath("$.data.profile").value(response.profile()))
                .andExpect(jsonPath("$.data.phoneNumber").value(response.phoneNumber()))
                .andExpect(jsonPath("$.data.nickName").value(response.nickName()))
                .andExpect(jsonPath("$.data.bank").isEmpty())
                .andExpect(jsonPath("$.data.accountNumber").isEmpty())
                .andExpect(jsonPath("$.data.accountHolder").isEmpty());
    }

    @Test
    @WithMockUser("SENIOR")
    @DisplayName("대학원생 마이페이지 계정 설정시 기존 정보를 조회한다")
    void getSeniorUserAccountWith() throws Exception {
        SeniorMyPageUserAccountResponse response = new SeniorMyPageUserAccountResponse(userOfSenior.getProfile(), userOfSenior.getPhoneNumber(), userOfSenior.getNickName(), "bank", "accountNumber", "accountHolder");
        given(seniorMyPageUseCase.getSeniorMyPageUserAccount(any()))
                .willReturn(response);

        mvc.perform(get("/senior/me/account")
                        .header(HttpHeaders.AUTHORIZATION, BEARER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_SENIOR_MYPAGE_ACCOUNT.getMessage()))
                .andExpect(jsonPath("$.data.profile").value(response.profile()))
                .andExpect(jsonPath("$.data.phoneNumber").value(response.phoneNumber()))
                .andExpect(jsonPath("$.data.nickName").value(response.nickName()))
                .andExpect(jsonPath("$.data.bank").value(response.bank()))
                .andExpect(jsonPath("$.data.accountNumber").value(response.accountNumber()))
                .andExpect(jsonPath("$.data.accountHolder").value(response.accountHolder()));
    }

    @Test
    @WithMockUser("SENIOR")
    @DisplayName("대학원생 마이페이지 계정을 설정한다")
    void updateSeniorUserAccount() throws Exception {
        String request = objectMapper.writeValueAsString(
                new SeniorMyPageUserAccountRequest("뉴닉", "01098765432", "profile", "98765", "국민", "예금주")
        );

        willDoNothing().given(seniorManageUseCase)
                        .updateSeniorMyPageUserAccount(any(), any());

        mvc.perform(patch("/senior/me/account")
                        .header(HttpHeaders.AUTHORIZATION, BEARER)
                        .with(csrf())
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_UPDATE.getCode()))
                .andExpect(jsonPath("$.message").value(UPDATE_MYPAGE_ACCOUNT.getMessage()));
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @WithMockUser
    @DisplayName("대학원생을 상세 조회한다")
    void getSeniorDetails(boolean tf) throws Exception {
        Info info = senior.getInfo();
        Profile profile = senior.getProfile();
        List<AvailableTimeResponse> availableTimeResponses = availables.stream()
                .map(available -> new AvailableTimeResponse(available.getDay(), available.getStartTime(), available.getEndTime()))
                .toList();
        SeniorDetailResponse response = new SeniorDetailResponse(
                tf, tf, userOfSenior.getNickName(), info.getTerm(), userOfSenior.getProfile(), info.getPostgradu(), info.getMajor(),
                info.getLab(), info.getProfessor(), info.getKeyword().split(","), profile.getInfo(), profile.getOneLiner(), profile.getTarget(),
                availableTimeResponses
        );

        given(seniorInfoUseCase.getSeniorDetail(any(), any()))
                .willReturn(response);

        mvc.perform(get("/senior/{seniorId}", senior.getSeniorId())
                        .header(HttpHeaders.AUTHORIZATION, BEARER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_SENIOR_INFO.getMessage()))
                .andExpect(jsonPath("$.data.isMine").value(tf))
                .andExpect(jsonPath("$.data.certification").value(tf))
                .andExpect(jsonPath("$.data.nickName").value(response.nickName()))
                .andExpect(jsonPath("$.data.term").value(response.term()))
                .andExpect(jsonPath("$.data.profile").value(response.profile()))
                .andExpect(jsonPath("$.data.postgradu").value(response.postgradu()))
                .andExpect(jsonPath("$.data.major").value(response.major()))
                .andExpect(jsonPath("$.data.lab").value(response.lab()))
                .andExpect(jsonPath("$.data.professor").value(response.professor()))
                .andExpect(jsonPath("$.data.keyword").isArray())
                .andExpect(jsonPath("$.data.info").value(response.info()))
                .andExpect(jsonPath("$.data.oneLiner").value(response.oneLiner()))
                .andExpect(jsonPath("$.data.target").value(response.target()))
                .andExpect(jsonPath("$.data.times").isArray());
    }

    @Test
    @WithMockUser
    @DisplayName("결제 시 대학원생의 기본 정보를 확인한다")
    void testGetSeniorProfile() throws Exception {
        Info info = senior.getInfo();
        SeniorProfileResponse response = new SeniorProfileResponse(
                userOfSenior.getNickName(), userOfSenior.getProfile(),
                info.getPostgradu(),info.getMajor(), info.getLab(), info.getTerm(),
                user.getUserId(), user.getPhoneNumber()
        );

        given(seniorInfoUseCase.getSeniorProfile(any(), any()))
                .willReturn(response);

        mvc.perform(get("/senior/{seniorId}/profile", senior.getSeniorId())
                        .header(HttpHeaders.AUTHORIZATION, BEARER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_SENIOR_INFO.getMessage()))
                .andExpect(jsonPath("$.data.nickName").value(response.nickName()))
                .andExpect(jsonPath("$.data.profile").value(response.profile()))
                .andExpect(jsonPath("$.data.major").value(response.major()))
                .andExpect(jsonPath("$.data.lab").value(response.lab()))
                .andExpect(jsonPath("$.data.term").value(response.term()))
                .andExpect(jsonPath("$.data.userId").value(response.userId()))
                .andExpect(jsonPath("$.data.phoneNumber").value(response.phoneNumber()));
    }

    @Test
    @WithMockUser("SENIOR")
    @DisplayName("신청서 작성 시 대학원생의 가능 시간 정보를 조회한다")
    void getSeniorTimes() throws Exception {
        List<AvailableTimeResponse> availableTimeResponses = availables.stream()
                .map(available -> new AvailableTimeResponse(available.getDay(), available.getStartTime(), available.getEndTime()))
                .toList();
        AvailableTimesResponse response = new AvailableTimesResponse(userOfSenior.getNickName(), availableTimeResponses);

        given(seniorInfoUseCase.getSeniorTimes(any()))
                .willReturn(response);

        mvc.perform(get("/senior/{seniorId}/times", senior.getSeniorId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_SENIOR_TIME.getMessage()))
                .andExpect(jsonPath("$.data.nickName").value(response.nickName()))
                .andExpect(jsonPath("$.data.times").isArray());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @WithMockUser
    @DisplayName("대학원생을 검색한다")
    void getSearchSenior(boolean tf) throws Exception {
        Info info = senior.getInfo();
        SeniorSearchResponse searchResponse = new SeniorSearchResponse(
                senior.getSeniorId(), tf, userOfSenior.getProfile(), userOfSenior.getNickName(),
                info.getPostgradu(), info.getMajor(), info.getLab(), info.getProfessor(), info.getKeyword().split(",")
        );
        List<SeniorSearchResponse> seniorSearchResponses = List.of(searchResponse, mock(SeniorSearchResponse.class), mock(SeniorSearchResponse.class));
        AllSeniorSearchResponse response = new AllSeniorSearchResponse(seniorSearchResponses, (long)seniorSearchResponses.size());

        given(seniorInfoUseCase.getSearchSenior(any(), any(), any()))
                .willReturn(response);

        mvc.perform(get("/senior/search", senior.getSeniorId())
                        .param("find", "keyword"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_SENIOR_LIST_INFO.getMessage()))
                .andExpect(jsonPath("$.data.seniorSearchResponses[0].seniorId").value(searchResponse.seniorId()))
                .andExpect(jsonPath("$.data.seniorSearchResponses[0].certification").value(tf))
                .andExpect(jsonPath("$.data.seniorSearchResponses[0].profile").value(searchResponse.profile()))
                .andExpect(jsonPath("$.data.seniorSearchResponses[0].nickName").value(searchResponse.nickName()))
                .andExpect(jsonPath("$.data.seniorSearchResponses[0].postgradu").value(searchResponse.postgradu()))
                .andExpect(jsonPath("$.data.seniorSearchResponses[0].major").value(searchResponse.major()))
                .andExpect(jsonPath("$.data.seniorSearchResponses[0].lab").value(searchResponse.lab()))
                .andExpect(jsonPath("$.data.seniorSearchResponses[0].keyword").isArray())
                .andExpect(jsonPath("$.data.totalElements").value(seniorSearchResponses.size()));
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @WithMockUser
    @DisplayName("대학원생을 분야와 대학교로 검색한다")
    void getFieldSenior(boolean tf) throws Exception {
        Info info = senior.getInfo();
        SeniorSearchResponse searchResponse = new SeniorSearchResponse(
                senior.getSeniorId(), tf, userOfSenior.getProfile(), userOfSenior.getNickName(),
                info.getPostgradu(), info.getMajor(), info.getLab(), info.getProfessor(), info.getKeyword().split(",")
        );
        List<SeniorSearchResponse> seniorSearchResponses = List.of(searchResponse, mock(SeniorSearchResponse.class), mock(SeniorSearchResponse.class));
        AllSeniorSearchResponse response = new AllSeniorSearchResponse(seniorSearchResponses, (long)seniorSearchResponses.size());

        given(seniorInfoUseCase.getFieldSenior(any(), any(), any()))
                .willReturn(response);

        mvc.perform(get("/senior/field", senior.getSeniorId())
                        .param("field", "field")
                        .param("postgradu", "postgradu"))
                .andExpect(status().isOk())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_SENIOR_LIST_INFO.getMessage()))
                .andExpect(jsonPath("$.data.seniorSearchResponses[0].seniorId").value(searchResponse.seniorId()))
                .andExpect(jsonPath("$.data.seniorSearchResponses[0].certification").value(tf))
                .andExpect(jsonPath("$.data.seniorSearchResponses[0].profile").value(searchResponse.profile()))
                .andExpect(jsonPath("$.data.seniorSearchResponses[0].nickName").value(searchResponse.nickName()))
                .andExpect(jsonPath("$.data.seniorSearchResponses[0].postgradu").value(searchResponse.postgradu()))
                .andExpect(jsonPath("$.data.seniorSearchResponses[0].major").value(searchResponse.major()))
                .andExpect(jsonPath("$.data.seniorSearchResponses[0].lab").value(searchResponse.lab()))
                .andExpect(jsonPath("$.data.seniorSearchResponses[0].keyword").isArray())
                .andExpect(jsonPath("$.data.totalElements").value(seniorSearchResponses.size()));
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @WithMockUser("SENIOR")
    @DisplayName("후배 전환시 가능 여부를 확인한다")
    void checkRole(boolean tf) throws Exception {
        SeniorPossibleResponse response = new SeniorPossibleResponse(tf, userOfSenior.getSocialId());

        given(seniorMyPageUseCase.checkUser(any()))
                .willReturn(response);

        mvc.perform(get("/senior/me/role")
                        .header(HttpHeaders.AUTHORIZATION, BEARER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_USER_CHECK.getMessage()))
                .andExpect(jsonPath("$.data.possible").value(tf))
                .andExpect(jsonPath("$.data.socialId").value(response.socialId()));
    }
}