package com.postgraduate.domain.senior.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postgraduate.IntegrationTest;
import com.postgraduate.domain.account.domain.entity.Account;
import com.postgraduate.domain.account.domain.repository.AccountRepository;
import com.postgraduate.domain.available.application.dto.req.AvailableCreateRequest;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.repository.SalaryRepository;
import com.postgraduate.domain.senior.application.dto.req.*;
import com.postgraduate.domain.senior.domain.entity.Info;
import com.postgraduate.domain.senior.domain.entity.Profile;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.entity.constant.Status;
import com.postgraduate.domain.senior.domain.repository.SeniorRepository;
import com.postgraduate.domain.senior.presentation.constant.SeniorResponseCode;
import com.postgraduate.domain.senior.presentation.constant.SeniorResponseMessage;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.entity.constant.Role;
import com.postgraduate.domain.user.domain.repository.UserRepository;
import com.postgraduate.global.config.security.jwt.util.JwtUtils;
import com.postgraduate.global.exception.constant.ErrorCode;
import com.postgraduate.global.exception.constant.ErrorMessage;
import com.postgraduate.global.slack.SlackMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.postgraduate.domain.salary.util.SalaryUtil.getSalaryDate;
import static com.postgraduate.domain.senior.presentation.constant.SeniorResponseCode.*;
import static com.postgraduate.domain.senior.presentation.constant.SeniorResponseMessage.*;
import static java.time.LocalDateTime.now;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SeniorControllerTest extends IntegrationTest {
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtUtils jwtUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SeniorRepository seniorRepository;
    @Autowired
    private SalaryRepository salaryRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AvailableRepository availableRepository;
    @MockBean
    private SlackMessage slackMessage;
    private Senior senior;
    private String token;

    @BeforeEach
    void setUp() throws IOException {
        User user = new User(0L, 1L, "mail", "후배", "011", "profile", 0, Role.SENIOR, true, now(), now(), false);
        userRepository.save(user);

        Info info = new Info("major", "postgradu", "교수님", "keyword1,keyword2", "랩실", "field", false, false, "field,keyword1,keyword2");
        senior = new Senior(0L, user, "certification", Status.APPROVE, 0, info, null, now(), now());
        seniorRepository.save(senior);

        token = jwtUtil.generateAccessToken(user.getUserId(), Role.SENIOR);

        doNothing().when(slackMessage).sendSlackLog(any());
    }


    private void updateProfile() {
        Profile profile = new Profile("저는요", "한줄소개", "대상", "chatLink", 30);
        senior.updateProfile(profile);
        seniorRepository.save(senior);
    }

    private void updateAvailable() {
        List<Available> availables = List.of(
                new Available(0L, "월", "17:00", "23:00", senior),
                new Available(0L, "금", "10:00", "20:00", senior),
                new Available(0L, "토", "10:00", "20:00", senior)
        );
        availableRepository.saveAll(availables);
    }

    @Test
    @DisplayName("대학원생 인증한다")
    void updateCertification() throws Exception {
        String request = objectMapper.writeValueAsString(
                new SeniorCertificationRequest("certification")
        );
        mvc.perform(patch("/senior/certification")
                        .header(AUTHORIZATION, BEARER + token)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_UPDATE.getCode()))
                .andExpect(jsonPath("$.message").value(UPDATE_CERTIFICATION.getMessage()));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("잘못된 이미지로 인증한다")
    void updateInvalidCertification(String certification) throws Exception {
        String request = objectMapper.writeValueAsString(
                new SeniorCertificationRequest(certification)
        );
        mvc.perform(patch("/senior/certification")
                        .header(AUTHORIZATION, BEARER + token)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ErrorCode.VALID_BLANK.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorMessage.VALID_BLANK.getMessage()));
    }

    @Test
    @DisplayName("대학원생 프로필을 등록한다")
    void singUpSenior() throws Exception {
        List<AvailableCreateRequest> availableCreateRequests = List.of(
                new AvailableCreateRequest("월", "17:00", "23:00"),
                new AvailableCreateRequest("금", "10:00", "20:00"),
                new AvailableCreateRequest("토", "10:00", "20:00")
        );
        String request = objectMapper.writeValueAsString(
                new SeniorProfileRequest("저는요", "대상", "chatLink", "한줄소개", availableCreateRequests)
        );

        mvc.perform(patch("/senior/profile")
                        .header(AUTHORIZATION, BEARER + token)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_UPDATE.getCode()))
                .andExpect(jsonPath("$.message").value(UPDATE_PROFILE.getMessage()));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("잘못된 대학원생 프로필을 등록한다")
    void singUpInvalidSenior(String empty) throws Exception {
        List<AvailableCreateRequest> availableCreateRequests = new ArrayList<>();
        String request = objectMapper.writeValueAsString(
                new SeniorProfileRequest(empty, empty, empty, empty, availableCreateRequests)
        );

        mvc.perform(patch("/senior/profile")
                        .header(AUTHORIZATION, BEARER + token)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ErrorCode.VALID_BLANK.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorMessage.VALID_BLANK.getMessage()));
    }

    @Test
    @DisplayName("월-일 외 요일을 입력하면 예외가 발생한다")
    void InvalidDay() throws Exception {
        List<AvailableCreateRequest> availableCreateRequests = List.of(
                new AvailableCreateRequest("월", "17:00", "23:00"),
                new AvailableCreateRequest("잉", "17:00", "23:00")
        );

        String request = objectMapper.writeValueAsString(
                new SeniorProfileRequest("저는요", "대상", "chatLink", "한줄소개", availableCreateRequests)
        );

        mvc.perform(patch("/senior/profile")
                        .header(AUTHORIZATION, BEARER + token)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SeniorResponseCode.INVALID_DAY.getCode()))
                .andExpect(jsonPath("$.message").value(SeniorResponseMessage.INVALID_DAY.getMessage()));
    }

    @Test
    @DisplayName("대학원생 정산 계좌를 생성한다")
    void updateAccount() throws Exception {
        Salary salary = new Salary(0L, false, senior, null, 10000, getSalaryDate(), now(), null, null, null);
        salaryRepository.save(salary);

        String request = objectMapper.writeValueAsString(
                new SeniorAccountRequest("농협", "주인", "123123123456")
        );

        mvc.perform(post("/senior/account")
                        .header(AUTHORIZATION, BEARER + token)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_CREATE.getCode()))
                .andExpect(jsonPath("$.message").value(CREATE_ACCOUNT.getMessage()));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("빈 정산 계좌를 입력으면 예외가 발생한다")
    void updateInvalidAccount(String empty) throws Exception {
        Salary salary = new Salary(0L, false, senior, null, 10000, getSalaryDate(), now(), null, null, null);
        salaryRepository.save(salary);

        String request = objectMapper.writeValueAsString(
                new SeniorAccountRequest(empty, empty, empty)
        );

        mvc.perform(post("/senior/account")
                        .header(AUTHORIZATION, BEARER + token)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ErrorCode.VALID_BLANK.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorMessage.VALID_BLANK.getMessage()));
    }

    @Test
    @DisplayName("대학원생 마이페이지 기본 정보를 조회한다")
    void getSeniorInfo() throws Exception {
        mvc.perform(get("/senior/me")
                        .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_SENIOR_INFO.getMessage()))
                .andExpect(jsonPath("$.data.seniorId").value(senior.getSeniorId()));
    }

    @Test
    @DisplayName("대학원생 마이페이지 프로필 수정시 기존 정보를 조회한다")
    void getSeniorProfile() throws Exception {
        updateProfile();
        updateAvailable();

        mvc.perform(get("/senior/me/profile")
                        .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_SENIOR_MYPAGE_PROFILE.getMessage()))
                .andExpect(jsonPath("$.data.lab").isNotEmpty())
                .andExpect(jsonPath("$.data.keyword").isNotEmpty())
                .andExpect(jsonPath("$.data.info").isNotEmpty())
                .andExpect(jsonPath("$.data.target").isNotEmpty())
                .andExpect(jsonPath("$.data.chatLink").isNotEmpty())
                .andExpect(jsonPath("$.data.field").isNotEmpty())
                .andExpect(jsonPath("$.data.oneLiner").isNotEmpty())
                .andExpect(jsonPath("$.data.times").exists());
    }

    @Test
    @DisplayName("등록된 프로필이 없다면 [내 프로필 수정] 기본 정보를 조회할 수 없다")
    void getEmptySeniorProfile() throws Exception {
        mvc.perform(get("/senior/me/profile")
                        .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SeniorResponseCode.NONE_PROFILE.getCode()))
                .andExpect(jsonPath("$.message").value(SeniorResponseMessage.NONE_PROFILE.getMessage()));
    }

    @Test
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

        mvc.perform(patch("/senior/me/profile")
                        .header(AUTHORIZATION, BEARER + token)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_UPDATE.getCode()))
                .andExpect(jsonPath("$.message").value(UPDATE_MYPAGE_PROFILE.getMessage()));
    }

    @Test
    @DisplayName("키워드가 6개 초과라면 예외가 발생한다")
    void updateInvalidKeyword() throws Exception {
        List<AvailableCreateRequest> availableCreateRequests = List.of(
                new AvailableCreateRequest("월", "17:00", "23:00"),
                new AvailableCreateRequest("금", "10:00", "20:00"),
                new AvailableCreateRequest("토", "10:00", "20:00")
        );
        String request = objectMapper.writeValueAsString(
                new SeniorMyPageProfileRequest("lab", "1,2,3,4,5,6,7", "info", "target", "chatLink", "AI", "oneliner", availableCreateRequests)
        );

        mvc.perform(patch("/senior/me/profile")
                        .header(AUTHORIZATION, BEARER + token)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SeniorResponseCode.INVALID_KEYWORD.getCode()))
                .andExpect(jsonPath("$.message").value(SeniorResponseMessage.INVALID_KEYWORD.getMessage()));
    }

    @Test
    @DisplayName("대학원생 마이페이지 계정 설정시 기존 정보를 조회한다")
    void getSeniorUserAccount() throws Exception {
        mvc.perform(get("/senior/me/account")
                        .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_SENIOR_MYPAGE_ACCOUNT.getMessage()));
    }

    @Test
    @DisplayName("대학원생 마이페이지 계정을 설정한다")
    void updateSeniorUserAccount() throws Exception {
        Salary salary = new Salary(0L, false, senior, null, 10000, getSalaryDate(), now(), null, null, null);
        salaryRepository.save(salary);

        String request = objectMapper.writeValueAsString(
                new SeniorMyPageUserAccountRequest("뉴닉", "01098765432", "profile", "98765", "국민", "예금주")
        );

        mvc.perform(patch("/senior/me/account")
                        .header(AUTHORIZATION, BEARER + token)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_UPDATE.getCode()))
                .andExpect(jsonPath("$.message").value(UPDATE_MYPAGE_ACCOOUNT.getMessage()));
    }

    @Test
    @DisplayName("계좌등록을 한 선배가 수정할 때 계좌를 입력하지 않으면 예외가 발생한다")
    void updateSeniorUserWithoutAccount() throws Exception {
        Account account = new Account(0L, "accountNumber", "bank", "accountHolder", senior);
        accountRepository.save(account);

        Salary salary = new Salary(0L, false, senior, null, 10000, getSalaryDate(), now(), null, null, null);
        salaryRepository.save(salary);

        String request = objectMapper.writeValueAsString(
                new SeniorMyPageUserAccountRequest("뉴닉", "01098765432", "profile", "", "", "")
        );

        mvc.perform(patch("/senior/me/account")
                        .header(AUTHORIZATION, BEARER + token)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SeniorResponseCode.NONE_ACCOUNT.getCode()))
                .andExpect(jsonPath("$.message").value(SeniorResponseMessage.NONE_ACCOUNT.getMessage()));
    }

    @Test
    @DisplayName("대학원생을 상세 조회한다")
    void getSeniorDetails() throws Exception {
        updateProfile();

        mvc.perform(get("/senior/{seniorId}", senior.getSeniorId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_SENIOR_INFO.getMessage()));
    }

    @ParameterizedTest
    @EnumSource(value = Status.class, names = {"NOT_APPROVE", "WAITING"})
    @DisplayName("승인되지 않은 대학원생은 조회되지 않는다.")
    void getNotApprovedSeniorDetails(Status status) throws Exception {
        senior.updateStatus(status);
        seniorRepository.save(senior);

        mvc.perform(get("/senior/{seniorId}", senior.getSeniorId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SeniorResponseCode.NONE_SENIOR.getCode()))
                .andExpect(jsonPath("$.message").value(SeniorResponseMessage.NONE_SENIOR.getMessage()));
    }

    @Test
    @DisplayName("결제 시 대학원생의 기본 정보를 확인한다")
    void testGetSeniorProfile() throws Exception {
        updateProfile();

        mvc.perform(get("/senior/{seniorId}/profile", senior.getSeniorId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_SENIOR_INFO.getMessage()));
    }

    @Test
    @DisplayName("신청서 작성 시 대학원생의 가능 시간 정보를 조회한다")
    void getSeniorTimes() throws Exception {
        updateProfile();

        mvc.perform(get("/senior/{seniorId}/times", senior.getSeniorId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_SENIOR_TIME.getMessage()));
    }

    @Test
    @DisplayName("대학원생을 검색한다")
    void getSearchSenior() throws Exception {
        mvc.perform(get("/senior/search", senior.getSeniorId())
                        .param("find", "keyword"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_SENIOR_LIST_INFO.getMessage()))
                .andExpect(jsonPath("$.data.seniorSearchResponses[0].seniorId").value(senior.getSeniorId()));
    }

    @Test
    @DisplayName("대학원생을 분야와 대학교로 검색한다")
    void getFieldSenior() throws Exception {
        mvc.perform(get("/senior/field", senior.getSeniorId())
                        .param("field", "field")
                        .param("postgradu", "postgradu"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_SENIOR_LIST_INFO.getMessage()))
                .andExpect(jsonPath("$.data.seniorSearchResponses[0].seniorId").value(senior.getSeniorId()));
    }

    @Test
    @DisplayName("후배 전환시 가능 여부를 확인한다")
    void checkRole() throws Exception {
        mvc.perform(get("/senior/me/role")
                        .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_USER_CHECK.getMessage()));
    }
}