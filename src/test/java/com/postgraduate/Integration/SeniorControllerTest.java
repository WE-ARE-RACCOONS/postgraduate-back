//package com.postgraduate.Integration;
//
//import com.postgraduate.domain.member.senior.application.dto.req.*;
//import com.postgraduate.domain.member.senior.domain.entity.Account;
//import com.postgraduate.domain.member.senior.domain.entity.Available;
//import com.postgraduate.domain.salary.domain.entity.Salary;
//import com.postgraduate.domain.member.senior.domain.entity.Senior;
//import com.postgraduate.domain.member.senior.presentation.constant.SeniorResponseCode;
//import com.postgraduate.domain.member.senior.presentation.constant.SeniorResponseMessage;
//import com.postgraduate.domain.member.user.domain.entity.User;
//import com.postgraduate.domain.member.user.domain.entity.constant.Role;
//import com.postgraduate.global.constant.ErrorCode;
//import com.postgraduate.support.IntegrationTest;
//import com.postgraduate.support.Resource;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.EmptySource;
//import org.junit.jupiter.params.provider.NullAndEmptySource;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.postgraduate.domain.member.senior.presentation.constant.SeniorResponseCode.*;
//import static com.postgraduate.domain.member.senior.presentation.constant.SeniorResponseMessage.*;
//import static java.lang.Boolean.FALSE;
//import static java.lang.Boolean.TRUE;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.doNothing;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//class SeniorControllerTest extends IntegrationTest {
//    private Resource resource = new Resource();
//    private static final String AUTHORIZATION = "Authorization";
//    private static final String BEARER = "Bearer ";
//    private Senior senior;
//    private Senior otherSenior;
//    private String token;
//    private String otherSeniorToken;
//    private String userToken;
//    private User user;
//    private User otherUser;
//
//    @BeforeEach
//    void setUp() throws IOException {
//        user = resource.getUser();
//        userRepository.save(user);
//
//        User seniorUser = resource.getSeniorUser();
//        userRepository.save(seniorUser);
//
//        otherUser = resource.getOtherUser();
//        userRepository.save(otherUser);
//
//        senior = resource.getSenior();
//        seniorRepository.save(senior);
//
//        otherSenior = resource.getOtherSenior();
//        seniorRepository.save(otherSenior);
//
//        Salary salary = resource.getSalary();
//        salaryRepository.save(salary);
//
//        List<Available> availables = resource.getAvailables();
//
//        token = jwtUtil.generateAccessToken(senior.getUser().getUserId(), Role.SENIOR);
//        otherSeniorToken = jwtUtil.generateAccessToken(otherSenior.getUser().getUserId(), Role.SENIOR);
//        userToken = jwtUtil.generateAccessToken(user.getUserId(), Role.USER);
//
//        doNothing().when(slackLogErrorMessage).sendSlackLog(any());
//    }
//
//    @Test
//    @DisplayName("대학원생 인증한다")
//    void updateCertification() throws Exception {
//        String request = objectMapper.writeValueAsString(
//                new SeniorCertificationRequest("certification")
//        );
//        mvc.perform(patch("/senior/certification")
//                        .header(AUTHORIZATION, BEARER + token)
//                        .content(request)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(SENIOR_UPDATE.getCode()))
//                .andExpect(jsonPath("$.message").value(UPDATE_CERTIFICATION.getMessage()));
//    }
//
//    @ParameterizedTest
//    @NullAndEmptySource
//    @WithMockUser(authorities = {"SENIOR"})
//    @DisplayName("잘못된 이미지로 인증한다")
//    void updateInvalidCertification(String certification) throws Exception {
//        String request = objectMapper.writeValueAsString(
//                new SeniorCertificationRequest(certification)
//        );
//        mvc.perform(patch("/senior/certification")
//                        .header(AUTHORIZATION, BEARER + token)
//                        .content(request)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(ErrorCode.VALID_BLANK.getCode()));
//    }
//
//    @Test
//    @DisplayName("대학원생 프로필을 등록한다")
//    void singUpSenior() throws Exception {
//        List<AvailableCreateRequest> availableCreateRequests = List.of(
//                new AvailableCreateRequest("월", "17:00", "23:00"),
//                new AvailableCreateRequest("금", "10:00", "20:00"),
//                new AvailableCreateRequest("토", "10:00", "20:00")
//        );
//        String request = objectMapper.writeValueAsString(
//                new SeniorProfileRequest("저는요", "대상", "한줄소개", availableCreateRequests)
//        );
//
//        mvc.perform(patch("/senior/profile")
//                        .header(AUTHORIZATION, BEARER + token)
//                        .content(request)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(SENIOR_UPDATE.getCode()))
//                .andExpect(jsonPath("$.message").value(UPDATE_PROFILE.getMessage()));
//    }
//
//    @ParameterizedTest
//    @NullAndEmptySource
//    @DisplayName("잘못된 대학원생 프로필을 등록한다")
//    void singUpInvalidSenior(String empty) throws Exception {
//        List<AvailableCreateRequest> availableCreateRequests = new ArrayList<>();
//        String request = objectMapper.writeValueAsString(
//                new SeniorProfileRequest(empty, empty, empty, availableCreateRequests)
//        );
//
//        mvc.perform(patch("/senior/profile")
//                        .header(AUTHORIZATION, BEARER + token)
//                        .content(request)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(ErrorCode.VALID_BLANK.getCode()));
//    }
//
//    @Test
//    @DisplayName("월-일 외 요일을 입력하면 예외가 발생한다")
//    void InvalidDay() throws Exception {
//        List<AvailableCreateRequest> availableCreateRequests = List.of(
//                new AvailableCreateRequest("월", "17:00", "23:00"),
//                new AvailableCreateRequest("잉", "17:00", "23:00")
//        );
//
//        String request = objectMapper.writeValueAsString(
//                new SeniorProfileRequest("저는요", "대상", "chatLink", availableCreateRequests)
//        );
//
//        mvc.perform(patch("/senior/profile")
//                        .header(AUTHORIZATION, BEARER + token)
//                        .content(request)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(SeniorResponseCode.INVALID_DAY.getCode()))
//                .andExpect(jsonPath("$.message").value(SeniorResponseMessage.INVALID_DAY.getMessage()));
//    }
//
//    @Test
//    @DisplayName("대학원생 정산 계좌를 생성한다")
//    void updateAccount() throws Exception {
//        String request = objectMapper.writeValueAsString(
//                new SeniorAccountRequest("123123123456", "농협", "주인")
//        );
//
//        mvc.perform(post("/senior/account")
//                        .header(AUTHORIZATION, BEARER + token)
//                        .content(request)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(SENIOR_CREATE.getCode()))
//                .andExpect(jsonPath("$.message").value(CREATE_ACCOUNT.getMessage()));
//    }
//
//    @ParameterizedTest
//    @NullAndEmptySource
//    @WithMockUser(authorities = {"SENIOR"})
//    @DisplayName("빈 정산 계좌를 입력으면 예외가 발생한다")
//    void updateInvalidAccount(String empty) throws Exception {
//        String request = objectMapper.writeValueAsString(
//                new SeniorAccountRequest(empty, empty, empty)
//        );
//
//        mvc.perform(post("/senior/account")
//                        .header(AUTHORIZATION, BEARER + token)
//                        .content(request)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(ErrorCode.VALID_BLANK.getCode()));
//    }
//
//    @Test
//    @DisplayName("대학원생 마이페이지 기본 정보를 조회한다")
//    void getSeniorInfo() throws Exception {
//        mvc.perform(get("/senior/me")
//                        .header(AUTHORIZATION, BEARER + token))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(SENIOR_FIND.getCode()))
//                .andExpect(jsonPath("$.message").value(GET_SENIOR_INFO.getMessage()))
//                .andExpect(jsonPath("$.data.seniorId").value(senior.getSeniorId()))
//                .andExpect(jsonPath("$.data.socialId").isNotEmpty())
//                .andExpect(jsonPath("$.data.nickName").isNotEmpty())
//                .andExpect(jsonPath("$.data.profile").isNotEmpty())
//                .andExpect(jsonPath("$.data.certificationRegister").isNotEmpty())
//                .andExpect(jsonPath("$.data.profileRegister").isNotEmpty());
//    }
//
//    @Test
//    @DisplayName("대학원생 마이페이지 프로필 수정시 기존 정보를 조회한다")
//    void getSeniorProfile() throws Exception {
//        mvc.perform(get("/senior/me/profile")
//                        .header(AUTHORIZATION, BEARER + token))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(SENIOR_FIND.getCode()))
//                .andExpect(jsonPath("$.message").value(GET_SENIOR_MYPAGE_PROFILE.getMessage()))
//                .andExpect(jsonPath("$.data.lab").isNotEmpty())
//                .andExpect(jsonPath("$.data.keyword").isNotEmpty())
//                .andExpect(jsonPath("$.data.info").isNotEmpty())
//                .andExpect(jsonPath("$.data.target").isNotEmpty())
//                .andExpect(jsonPath("$.data.chatLink").isNotEmpty())
//                .andExpect(jsonPath("$.data.field").isNotEmpty())
//                .andExpect(jsonPath("$.data.oneLiner").isNotEmpty())
//                .andExpect(jsonPath("$.data.times").exists());
//    }
//
//    @Test
//    @DisplayName("등록된 프로필이 없다면 [내 프로필 수정] 기본 정보를 조회할 수 없다")
//    void getEmptySeniorProfile() throws Exception {
//        mvc.perform(get("/senior/me/profile")
//                        .header(AUTHORIZATION, BEARER + otherSeniorToken))
//                        .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(SENIOR_FIND.getCode()))
//                .andExpect(jsonPath("$.message").value(GET_SENIOR_MYPAGE_PROFILE.getMessage()))
//                .andExpect(jsonPath("$.data.lab").isNotEmpty())
//                .andExpect(jsonPath("$.data.keyword").isNotEmpty())
//                .andExpect(jsonPath("$.data.info").isEmpty())
//                .andExpect(jsonPath("$.data.target").isEmpty())
//                .andExpect(jsonPath("$.data.chatLink").isNotEmpty())
//                .andExpect(jsonPath("$.data.field").isNotEmpty())
//                .andExpect(jsonPath("$.data.oneLiner").isEmpty())
//                .andExpect(jsonPath("$.data.times").isEmpty());
//    }
//
//    @Test
//    @DisplayName("대학원생 마이페이지 프로필을 수정한다")
//    void updateSeniorProfile() throws Exception {
//        List<AvailableCreateRequest> availableCreateRequests = List.of(
//                new AvailableCreateRequest("월", "17:00", "23:00"),
//                new AvailableCreateRequest("금", "10:00", "20:00"),
//                new AvailableCreateRequest("토", "10:00", "20:00")
//        );
//        String request = objectMapper.writeValueAsString(
//                new SeniorMyPageProfileRequest("lab", "keyword1,keyword2", "info", "target", "chatLink", "AI", "oneliner", availableCreateRequests)
//        );
//
//        mvc.perform(patch("/senior/me/profile")
//                        .header(AUTHORIZATION, BEARER + token)
//                        .content(request)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(SENIOR_UPDATE.getCode()))
//                .andExpect(jsonPath("$.message").value(UPDATE_MYPAGE_PROFILE.getMessage()));
//    }
//
//    @ParameterizedTest
//    @NullAndEmptySource
//    @WithMockUser(authorities = {"SENIOR"})
//    @DisplayName("가능 시간대가 비어있으면 예외가 발생한다")
//    void updateInvalidAvailableSeniorProfile(String empty) throws Exception {
//        List<AvailableCreateRequest> availableCreateRequests = List.of(
//                new AvailableCreateRequest(empty, empty, empty),
//                new AvailableCreateRequest(empty, empty, empty),
//                new AvailableCreateRequest(empty, empty, empty)
//        );
//
//        String request = objectMapper.writeValueAsString(
//                new SeniorMyPageProfileRequest("lab", "keyword1,keyword2", "info", "target", "chatLink", "AI", "oneliner", availableCreateRequests)
//        );
//
//        mvc.perform(patch("/senior/me/profile")
//                        .header(AUTHORIZATION, BEARER + token)
//                        .content(request)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(ErrorCode.VALID_BLANK.getCode()));
//    }
//
//    @ParameterizedTest
//    @NullAndEmptySource
//    @WithMockUser(authorities = {"SENIOR"})
//    @DisplayName("프로필이 비어있으면 예외가 발생한다")
//    void updateInvalidSeniorProfile(String empty) throws Exception {
//        List<AvailableCreateRequest> availableCreateRequests = List.of(
//                new AvailableCreateRequest("월", "17:00", "23:00"),
//                new AvailableCreateRequest("금", "10:00", "20:00"),
//                new AvailableCreateRequest("토", "10:00", "20:00")
//        );
//
//        String request = objectMapper.writeValueAsString(
//                new SeniorMyPageProfileRequest(empty, empty, empty, empty, empty, empty, empty, availableCreateRequests)
//        );
//
//        mvc.perform(patch("/senior/me/profile")
//                        .header(AUTHORIZATION, BEARER + token)
//                        .content(request)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(ErrorCode.VALID_BLANK.getCode()));
//    }
//
//    @Test
//    @DisplayName("키워드가 6개 초과라면 예외가 발생한다")
//    void updateInvalidKeyword() throws Exception {
//        List<AvailableCreateRequest> availableCreateRequests = List.of(
//                new AvailableCreateRequest("월", "17:00", "23:00"),
//                new AvailableCreateRequest("금", "10:00", "20:00"),
//                new AvailableCreateRequest("토", "10:00", "20:00")
//        );
//        String request = objectMapper.writeValueAsString(
//                new SeniorMyPageProfileRequest("lab", "1,2,3,4,5,6,7", "info", "target", "chatLink", "AI", "oneliner", availableCreateRequests)
//        );
//
//        mvc.perform(patch("/senior/me/profile")
//                        .header(AUTHORIZATION, BEARER + token)
//                        .content(request)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(SeniorResponseCode.INVALID_KEYWORD.getCode()))
//                .andExpect(jsonPath("$.message").value(SeniorResponseMessage.INVALID_KEYWORD.getMessage()));
//    }
//
//    @Test
//    @DisplayName("등록한 계좌가 없다면 null을 반환한다.")
//    void getSeniorUserEmptyAccount() throws Exception {
//        mvc.perform(get("/senior/me/account")
//                        .header(AUTHORIZATION, BEARER + token))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(SENIOR_FIND.getCode()))
//                .andExpect(jsonPath("$.message").value(GET_SENIOR_MYPAGE_ACCOUNT.getMessage()))
//                .andExpect(jsonPath("$.data.profile").isNotEmpty())
//                .andExpect(jsonPath("$.data.phoneNumber").isNotEmpty())
//                .andExpect(jsonPath("$.data.nickName").isNotEmpty())
//                .andExpect(jsonPath("$.data.bank").isEmpty())
//                .andExpect(jsonPath("$.data.accountNumber").isEmpty())
//                .andExpect(jsonPath("$.data.accountHolder").isEmpty());
//    }
//
//    @Test
//    @DisplayName("대학원생 마이페이지 계정 설정시 기존 정보를 조회한다")
//    void getSeniorUserAccount() throws Exception {
//        updateAccount();
//        mvc.perform(get("/senior/me/account")
//                        .header(AUTHORIZATION, BEARER + token))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(SENIOR_FIND.getCode()))
//                .andExpect(jsonPath("$.message").value(GET_SENIOR_MYPAGE_ACCOUNT.getMessage()))
//                .andExpect(jsonPath("$.data.profile").isNotEmpty())
//                .andExpect(jsonPath("$.data.phoneNumber").isNotEmpty())
//                .andExpect(jsonPath("$.data.nickName").isNotEmpty())
//                .andExpect(jsonPath("$.data.bank").isNotEmpty())
//                .andExpect(jsonPath("$.data.accountNumber").isNotEmpty())
//                .andExpect(jsonPath("$.data.accountHolder").isNotEmpty());
//    }
//
//    @Test
//    @DisplayName("대학원생 마이페이지 계정을 설정한다")
//    void updateSeniorUserAccount() throws Exception {
//        String request = objectMapper.writeValueAsString(
//                new SeniorMyPageUserAccountRequest("뉴닉", "01098765432", "profile", "98765", "국민", "예금주")
//        );
//
//        mvc.perform(patch("/senior/me/account")
//                        .header(AUTHORIZATION, BEARER + token)
//                        .content(request)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(SENIOR_UPDATE.getCode()))
//                .andExpect(jsonPath("$.message").value(UPDATE_MYPAGE_ACCOUNT.getMessage()));
//    }
//
//    @ParameterizedTest
//    @NullAndEmptySource
//    @WithMockUser(authorities = {"SENIOR"})
//    @DisplayName("대학원생 마이페이지 계정을 수정 요청에 닉네임, 전화번호, 프로필사진이 없다면 예외가 발생한다")
//    void updateEmptySeniorUserAccount(String empty) throws Exception {
//        String request = objectMapper.writeValueAsString(
//                new SeniorMyPageUserAccountRequest(empty, empty, empty, "98765", "국민", "예금주")
//        );
//
//        mvc.perform(patch("/senior/me/account")
//                        .header(AUTHORIZATION, BEARER + token)
//                        .content(request)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(ErrorCode.VALID_BLANK.getCode()));
//    }
//
//    @ParameterizedTest
//    @EmptySource
//    @DisplayName("계좌등록을 한 선배가 수정할 때 계좌를 입력하지 않으면 예외가 발생한다")
//    void updateSeniorUserWithoutAccount(String empty) throws Exception {
//        String request = objectMapper.writeValueAsString(
//                new SeniorMyPageUserAccountRequest("뉴닉", "01098765432", "profile", empty, empty, empty)
//        );
//
//        Account account = resource.getAccount();
//
//        mvc.perform(patch("/senior/me/account")
//                        .header(AUTHORIZATION, BEARER + token)
//                        .content(request)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(ACCOUNT_NOT_FOUND.getCode()))
//                .andExpect(jsonPath("$.message").value(NOT_FOUND_ACCOUNT.getMessage()));
//    }
//
//    @Test
//    @DisplayName("대학원생을 상세 조회한다 - 본인 조회")
//    void getSeniorDetails() throws Exception {
//        mvc.perform(get("/senior/{seniorId}", senior.getSeniorId())
//                        .header(AUTHORIZATION, BEARER + token))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(SENIOR_FIND.getCode()))
//                .andExpect(jsonPath("$.message").value(GET_SENIOR_INFO.getMessage()))
//                .andExpect(jsonPath("$.data.isMine").value(TRUE))
//                .andExpect(jsonPath("$.data.nickName").isNotEmpty())
//                .andExpect(jsonPath("$.data.term").isNotEmpty())
//                .andExpect(jsonPath("$.data.profile").isNotEmpty())
//                .andExpect(jsonPath("$.data.postgradu").isNotEmpty())
//                .andExpect(jsonPath("$.data.major").isNotEmpty())
//                .andExpect(jsonPath("$.data.lab").isNotEmpty())
//                .andExpect(jsonPath("$.data.professor").isNotEmpty())
//                .andExpect(jsonPath("$.data.keyword").isNotEmpty())
//                .andExpect(jsonPath("$.data.info").isNotEmpty())
//                .andExpect(jsonPath("$.data.oneLiner").isNotEmpty())
//                .andExpect(jsonPath("$.data.target").isNotEmpty())
//                .andExpect(jsonPath("$.data.times").isNotEmpty());
//    }
//
//    @Test
//    @WithMockUser(authorities = {"USER", "SENIOR", "ADMIN"})
//    @DisplayName("대학원생을 상세 조회한다 - 타인 조회")
//    void getSeniorDetailsOthers() throws Exception {
//        mvc.perform(get("/senior/{seniorId}", senior.getSeniorId())
//                        .header(AUTHORIZATION, BEARER + userToken))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(SENIOR_FIND.getCode()))
//                .andExpect(jsonPath("$.message").value(GET_SENIOR_INFO.getMessage()))
//                .andExpect(jsonPath("$.data.isMine").value(FALSE))
//                .andExpect(jsonPath("$.data.nickName").isNotEmpty())
//                .andExpect(jsonPath("$.data.term").isNotEmpty())
//                .andExpect(jsonPath("$.data.profile").isNotEmpty())
//                .andExpect(jsonPath("$.data.postgradu").isNotEmpty())
//                .andExpect(jsonPath("$.data.major").isNotEmpty())
//                .andExpect(jsonPath("$.data.lab").isNotEmpty())
//                .andExpect(jsonPath("$.data.professor").isNotEmpty())
//                .andExpect(jsonPath("$.data.keyword").isNotEmpty())
//                .andExpect(jsonPath("$.data.info").isNotEmpty())
//                .andExpect(jsonPath("$.data.oneLiner").isNotEmpty())
//                .andExpect(jsonPath("$.data.target").isNotEmpty())
//                .andExpect(jsonPath("$.data.times").isNotEmpty());
//    }
//
//    @Test
//    @WithMockUser(authorities = {"USER"})
//    @DisplayName("결제 시 대학원생의 기본 정보를 확인한다")
//    void testGetSeniorProfile() throws Exception {
//        mvc.perform(get("/senior/{seniorId}/profile", senior.getSeniorId())
//                        .header(AUTHORIZATION, BEARER + userToken))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(SENIOR_FIND.getCode()))
//                .andExpect(jsonPath("$.message").value(GET_SENIOR_INFO.getMessage()))
//                .andExpect(jsonPath("$.data.nickName").isNotEmpty())
//                .andExpect(jsonPath("$.data.profile").isNotEmpty())
//                .andExpect(jsonPath("$.data.major").isNotEmpty())
//                .andExpect(jsonPath("$.data.lab").isNotEmpty())
//                .andExpect(jsonPath("$.data.term").isNotEmpty())
//                .andExpect(jsonPath("$.data.userId").isNotEmpty())
//                .andExpect(jsonPath("$.data.phoneNumber").isNotEmpty());
//    }
//
//    @Test
//    @WithMockUser(authorities = {"USER"})
//    @DisplayName("신청서 작성 시 대학원생의 가능 시간 정보를 조회한다")
//    void getSeniorTimes() throws Exception {
//        mvc.perform(get("/senior/{seniorId}/times", senior.getSeniorId()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(SENIOR_FIND.getCode()))
//                .andExpect(jsonPath("$.message").value(GET_SENIOR_TIME.getMessage()))
//                .andExpect(jsonPath("$.data.nickName").isNotEmpty())
//                .andExpect(jsonPath("$.data.times").isNotEmpty());
//    }
//
//    @Test
//    @DisplayName("대학원생을 검색한다")
//    void getSearchSenior() throws Exception {
//        mvc.perform(get("/senior/search", senior.getSeniorId())
//                        .param("find", senior.getInfo().getKeyword()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(SENIOR_FIND.getCode()))
//                .andExpect(jsonPath("$.message").value(GET_SENIOR_LIST_INFO.getMessage()))
//                .andExpect(jsonPath("$.data.seniorSearchResponses[0].seniorId").isNotEmpty())
//                .andExpect(jsonPath("$.data.seniorSearchResponses[0].profile").isNotEmpty())
//                .andExpect(jsonPath("$.data.seniorSearchResponses[0].nickName").isNotEmpty())
//                .andExpect(jsonPath("$.data.seniorSearchResponses[0].postgradu").isNotEmpty())
//                .andExpect(jsonPath("$.data.seniorSearchResponses[0].major").isNotEmpty())
//                .andExpect(jsonPath("$.data.seniorSearchResponses[0].lab").isNotEmpty())
//                .andExpect(jsonPath("$.data.seniorSearchResponses[0].keyword").isNotEmpty());
//    }
//
//    @Test
//    @DisplayName("대학원생을 분야와 대학교로 검색한다")
//    void getFieldSenior() throws Exception {
//        mvc.perform(get("/senior/field", senior.getSeniorId())
//                        .param("field", senior.getInfo().getField())
//                        .param("postgradu", senior.getInfo().getPostgradu()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(SENIOR_FIND.getCode()))
//                .andExpect(jsonPath("$.message").value(GET_SENIOR_LIST_INFO.getMessage()))
//                .andExpect(jsonPath("$.data.seniorSearchResponses[0].seniorId").isNotEmpty())
//                .andExpect(jsonPath("$.data.seniorSearchResponses[0].profile").isNotEmpty())
//                .andExpect(jsonPath("$.data.seniorSearchResponses[0].nickName").isNotEmpty())
//                .andExpect(jsonPath("$.data.seniorSearchResponses[0].postgradu").isNotEmpty())
//                .andExpect(jsonPath("$.data.seniorSearchResponses[0].major").isNotEmpty())
//                .andExpect(jsonPath("$.data.seniorSearchResponses[0].lab").isNotEmpty())
//                .andExpect(jsonPath("$.data.seniorSearchResponses[0].keyword").isNotEmpty());
//    }
//
//    @Test
//    @DisplayName("후배 전환시 가능 여부를 확인한다")
//    void checkRole() throws Exception {
//        mvc.perform(get("/senior/me/role")
//                        .header(AUTHORIZATION, BEARER + token))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(SENIOR_FIND.getCode()))
//                .andExpect(jsonPath("$.message").value(GET_USER_CHECK.getMessage()))
//                .andExpect(jsonPath("$.data.possible").isNotEmpty());
//    }
//}